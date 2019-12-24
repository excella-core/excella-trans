/*-
 * #%L
 * excella-trans
 * %%
 * Copyright (C) 2009 - 2019 bBreak Systems and contributors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package org.bbreak.excella.trans.tag.sheet2java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.BookController;
import org.bbreak.excella.core.SheetData;
import org.bbreak.excella.core.SheetParser;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.core.listener.SheetParseListener;
import org.bbreak.excella.core.tag.TagParser;
import org.bbreak.excella.core.util.PoiUtil;
import org.bbreak.excella.core.util.TagUtil;
import org.bbreak.excella.trans.tag.sheet2java.model.SheetToJavaParseInfo;
import org.bbreak.excella.trans.tag.sheet2java.model.SheetToJavaSettingInfo;

/**
 * シートを解析し、Javaオブジェクトを<BR>
 * List&lt;Object&gt;で取得する<BR>
 * 
 * @since 1.0
 */
public class SheetToJavaExecuter implements SheetParseListener {

    /**
     * 論理名パラメータ定義の開始文字
     */
    protected static final String LNAME_TAG_PARAM_PREFIX = "(";

    /**
     * 論理名パラメータ定義の終了文字
     */
    protected static final String LNAME_TAG_PARAM_SUFFIX = ")";

    /**
     * タグ定義の開始文字
     */
    protected static final String TAG_PREFIX = "@";

    /**
     * 論理名タグ
     */
    protected static final String TAG_LOGICAL_NAME_PREFIX = "@LNAME(";

    /**
     * カスタムプロパティのリスト
     */
    private List<SheetToJavaPropertyParser> customPropertyParsers = new ArrayList<SheetToJavaPropertyParser>();

    /**
     * 処理リスナのリスト
     */
    private List<SheetToJavaListener> sheetToJavaListeners = new ArrayList<SheetToJavaListener>();

    /**
     * 解析前処理
     * 
     * @param sheet 対象シート
     * @param sheetParser 対象シートパーサ
     */
    public void preParse( Sheet sheet, SheetParser sheetParser) {
        // do nothing
    }

    /**
     * 解析後処理<BR>
     * 設定情報に基づき、データをオブジェクトに変換し、<BR>
     * 結果をリストに入れて、SheetDataにセットする<BR>
     * 
     * @param sheet 対象シート
     * @param sheetParser 対象シートパーサ
     * @param sheetData 解析結果シートデータ
     */
    @SuppressWarnings( "unchecked")
    public void postParse( Sheet sheet, SheetParser sheetParser, SheetData sheetData) throws ParseException {

        // 結果オブジェクトのリスト
        List<Object> results = new ArrayList<Object>();

        // 対象シートにSheetToJavaParserが存在するか
        List<TagParser<?>> tagParsers = sheetParser.getTagParsers();

        // 処理後不要になるSheetToJavaSettingParserの
        // データを判別するタグのリスト
        List<String> removeTags = new ArrayList<String>();

        // 処理をするタグの一覧を生成
        List<String> targetTags = new ArrayList<String>();
        for ( TagParser<?> tagParser : tagParsers) {
            // SheetToJavaParserのタグ
            if ( tagParser instanceof SheetToJavaParser) {
                targetTags.add( tagParser.getTag());
            }
            // SheetToJavaSettingParserのタグ
            if ( tagParser instanceof SheetToJavaSettingParser) {
                removeTags.add( tagParser.getTag());
            }
        }

        // ワークブック取得
        Workbook workbook = sheet.getWorkbook();

        // 処理対象のタグでループ
        for ( String tag : targetTags) {

            List<SheetToJavaParseInfo> sheetInfoList = ( List<SheetToJavaParseInfo>) sheetData.get( tag);

            if ( sheetInfoList == null) {
                continue;
            }

            // 設定情報(シート)単位でループ
            for ( SheetToJavaParseInfo sheetInfo : sheetInfoList) {

                List<SheetToJavaSettingInfo> allColumnInfoList = ( List<SheetToJavaSettingInfo>) sheetData.get( sheetInfo.getSettingTagName());

                // 今回処理対象シート分の設定を取得
                List<SheetToJavaSettingInfo> targetColumnInfoList = new ArrayList<SheetToJavaSettingInfo>();
                for ( SheetToJavaSettingInfo columnInfo : allColumnInfoList) {
                    if ( columnInfo.getSheetName().equals( sheetInfo.getSheetName())) {
                        targetColumnInfoList.add( columnInfo);
                    }
                }

                // 対象シートデータの読み込み
                Sheet targetSheet = workbook.getSheet( sheetInfo.getSheetName());
                if ( targetSheet == null) {
                    throw new ParseException( sheetInfo.getSheetNameCell(), "シート[" + sheetInfo.getSheetName() + "]は存在しません");
                }
                results.addAll( parseTargetSheet( targetSheet, sheetInfo, targetColumnInfoList));
            }

            // sheetDataに結果を格納
            sheetData.put( tag, results);
        }

        // 処理後に不要になるデータの削除
        for ( String removeTag : removeTags) {
            sheetData.remove( removeTag);
        }
    }

    /**
     * 設定情報に基づき、対象シートをオブジェクトに<BR>
     * 変換し、リストに入れて返却する<BR>
     * 
     * @param targetSheet 解析対象シート
     * @param targetColumnInfoList 設定情報
     * @return オブジェクトリスト
     * @throws ParseException パース例外
     */
    protected List<Object> parseTargetSheet( Sheet targetSheet, SheetToJavaParseInfo sheetInfo, List<SheetToJavaSettingInfo> targetColumnInfoList) throws ParseException {

        // 結果オブジェクトのリスト
        List<Object> results = new ArrayList<Object>();

        int logicalRowNum = sheetInfo.getLogicalNameRowNum() - 1;
        int valueStartRowNum = sheetInfo.getValueRowNum() - 1;
        int valueEndRowNum = targetSheet.getLastRowNum();

        // 論理名、対応カラムindexのマップ
        Map<String, Integer> colLogicalNameMap = new HashMap<String, Integer>();

        // colLogicalNameMap作成
        Row row = targetSheet.getRow( logicalRowNum);
        if ( row != null) {

            // 論理名行の開始列と終了列
            int firstColIdx = row.getFirstCellNum();
            int lastColIdx = row.getLastCellNum();

            for ( int colIdx = firstColIdx; colIdx <= lastColIdx; colIdx++) {
                Cell cell = row.getCell( colIdx);
                if ( cell != null) {
                    try {
                        // 論理名
                        String logicalCellValue = cell.getStringCellValue();
                        if ( !logicalCellValue.startsWith( BookController.COMMENT_PREFIX)) {
                            colLogicalNameMap.put( logicalCellValue, colIdx);
                        }
                    } catch ( Exception e) {
                        throw new ParseException( cell, e);
                    }
                }
            }
        }

        // 定義順に処理する必要があるため、データの順番を保持するリスト
        List<Class<?>> classList = new ArrayList<Class<?>>();

        // クラス、SettingInfoリストのマップ
        Map<Class<?>, List<SheetToJavaSettingInfo>> settingInfoListMap = new HashMap<Class<?>, List<SheetToJavaSettingInfo>>();
        // クラス、重複不可プロパティ名リストのマップ
        Map<Class<?>, List<String>> uniquePropertyListMap = new HashMap<Class<?>, List<String>>();
        for ( SheetToJavaSettingInfo settingInfo : targetColumnInfoList) {

            // マップからリストを取得
            Class<?> clazz = settingInfo.getClazz();
            List<SheetToJavaSettingInfo> settingInfoList = settingInfoListMap.get( clazz);
            if ( settingInfoList == null) {
                // 取得できなかった場合
                settingInfoList = new ArrayList<SheetToJavaSettingInfo>();
            }
            List<String> uniquePropertyList = uniquePropertyListMap.get( clazz);
            if ( uniquePropertyList == null) {
                // 取得できなかった場合
                uniquePropertyList = new ArrayList<String>();
            }

            // リストに追加する
            settingInfoList.add( settingInfo);
            if ( settingInfo.isUnique()) {
                uniquePropertyList.add( settingInfo.getPropertyName());
            }

            // データの順番を保持
            if ( !classList.contains( clazz)) {
                classList.add( clazz);
            }

            // マップに詰める
            settingInfoListMap.put( clazz, settingInfoList);
            uniquePropertyListMap.put( clazz, uniquePropertyList);
        }

        // クラスごとのループ
        for ( Class<?> clazz : classList) {

            // 結果オブジェクトのリスト
            List<Object> objList = new ArrayList<Object>();

            Object obj = null;
            try {

                // データ行ごとのループ
                for ( int valueRowIdx = valueStartRowNum; valueRowIdx <= valueEndRowNum; valueRowIdx++) {
                    Row valueRow = targetSheet.getRow( valueRowIdx);
                    if ( valueRow == null) {
                        continue;
                    }

                    boolean isProcessRow = true;
                    for ( SheetToJavaListener propertyListener : sheetToJavaListeners) {
                        if ( !propertyListener.preProcessRow( valueRow)) {
                            isProcessRow = false;
                        }
                    }
                    if ( !isProcessRow) {
                        continue;
                    }

                    obj = Class.forName( clazz.getName()).newInstance();

                    // プロパティごとのループ
                    List<SheetToJavaSettingInfo> settingInfoList = settingInfoListMap.get( clazz);
                    for ( SheetToJavaSettingInfo settingInfo : settingInfoList) {

                        // プロパティ名
                        String propertyName = settingInfo.getPropertyName();
                        // 値
                        Object value = settingInfo.getValue();
                        // セットする
                        Object settingValue = value;
                        Cell valueCell = null;

                        if ( value instanceof String) {
                            // 文字列の場合
                            String settingValueStr = ( String) value;
                            if ( settingValueStr.startsWith( TAG_PREFIX)) {
                                // タグ定義の場合
                                if ( settingValueStr.startsWith( TAG_LOGICAL_NAME_PREFIX)) {
                                    // 論理名の場合
                                    String logicalKey = TagUtil.getParam( settingValueStr, LNAME_TAG_PARAM_PREFIX, LNAME_TAG_PARAM_SUFFIX);
                                    Integer logicalKeyCol = colLogicalNameMap.get( logicalKey);
                                    if ( logicalKeyCol == null) {
                                        Cell errorCell = null;
                                        for ( SheetToJavaSettingInfo columnInfo : targetColumnInfoList) {
                                            if ( columnInfo.getValue().equals( settingValueStr)) {
                                                errorCell = columnInfo.getValueCell();
                                            }
                                        }
                                        throw new ParseException( errorCell, "論理名タグパラメータ不正:" + logicalKey);
                                    }

                                    valueCell = valueRow.getCell( logicalKeyCol);
                                    if ( valueCell != null) {
                                        Class<?> propertyClass = PropertyUtils.getPropertyType( obj, settingInfo.getPropertyName());
                                        try {
                                            settingValue = PoiUtil.getCellValue( valueCell, propertyClass);
                                        } catch ( RuntimeException e) {
                                            throw new ParseException( valueCell, "値の取得に失敗しました(" + propertyClass + ")", e);
                                        }
                                    } else {
                                        // セルがnullの場合
                                        settingValue = null;
                                        valueCell = null;
                                    }

                                } else {
                                    // それ以外のタグの場合
                                    // カスタムパーサの処理実行
                                    parseCustomProperty( valueCell, colLogicalNameMap, obj, valueRow, settingValueStr);
                                    // 次のループへ
                                    continue;
                                }
                            }
                        }

                        // 値をセット
                        try {
                            // プロパティ設定前処理の呼び出し
                            for ( SheetToJavaListener propertyListener : sheetToJavaListeners) {
                                propertyListener.preSetProperty( valueCell, obj, propertyName, settingValue);
                            }

                            PropertyUtils.setProperty( obj, propertyName, settingValue);

                            // プロパティ設定後処理の呼び出し
                            for ( SheetToJavaListener propertyListener : sheetToJavaListeners) {
                                propertyListener.postSetProperty( valueCell, obj, propertyName, settingValue);
                            }
                        } catch ( ParseException parseEx) {
                            throw parseEx;
                        } catch ( RuntimeException e) {
                            throw new ParseException( valueCell, "値の設定に失敗しました(" + propertyName + "=" + settingValue + "[" + settingValue.getClass().getCanonicalName() + "]" + ")", e);
                        }
                    }

                    for ( SheetToJavaListener propertyListener : sheetToJavaListeners) {
                        if ( !propertyListener.postProcessRow( valueRow, obj)) {
                            isProcessRow = false;
                        }
                    }
                    if ( !isProcessRow) {
                        continue;
                    }

                    List<String> uniquePropertyList = uniquePropertyListMap.get( clazz);
                    if ( !isDuplicateObj( obj, objList, uniquePropertyList)) {
                        // 重複していない場合
                        objList.add( obj);
                    }
                }

                // 結果リストに格納する
                results.addAll( objList);
            } catch ( ParseException parseEx) {
                throw parseEx;
            } catch ( Exception e) {
                throw new ParseException( e.toString());
            }
        }

        return results;
    }

    /**
     * カスタムパーサの処理を実行する
     * 
     * @parma valueCell 値を持つセル
     * @param colLogicalNameMap 論理名、カラムindexのマップ
     * @param obj 対象オブジェクト
     * @param valueRow 値列
     * @param tag タグ
     * @throws ParseException パース例外
     */
    private void parseCustomProperty( Cell valueCell, Map<String, Integer> colLogicalNameMap, Object obj, Row valueRow, String tag) throws ParseException {

        for ( SheetToJavaPropertyParser propertyParser : customPropertyParsers) {

            // カスタムプロパティの判定
            if ( propertyParser.isParse( tag)) {

                // パラメータ内で定義されているプロパティ、値のマップ
                Map<String, String> paramMap = TagUtil.getParams( tag);
                Set<String> paramKeys = paramMap.keySet();

                Map<String, Object> paramValueMap = new HashMap<String, Object>();
                Map<String, Cell> paramCellMap = new HashMap<String, Cell>();
                for ( String paramKey : paramKeys) {

                    String paramValue = paramMap.get( paramKey);
                    if ( paramValue.startsWith( TAG_LOGICAL_NAME_PREFIX)) {
                        // 論理名の場合
                        String logicalMapKey = TagUtil.getParam( paramValue, LNAME_TAG_PARAM_PREFIX, LNAME_TAG_PARAM_SUFFIX);
                        Integer logicalKeyCol = colLogicalNameMap.get( logicalMapKey);
                        Cell cell = valueRow.getCell( logicalKeyCol);
                        Object cellValue = PoiUtil.getCellValue( cell);
                        paramValueMap.put( paramKey, cellValue);
                        paramCellMap.put( paramKey, cell);

                    } else {
                        // 固定値の場合
                        paramValueMap.put( paramKey, paramValue);
                    }
                }
                // カスタムパーサの処理実行
                propertyParser.parse( obj, paramCellMap, paramValueMap);
            }
        }
    }

    /**
     * 重複オブジェクトが存在するかを判定する。<BR>
     * リストの中に対象オブジェクトとユニークプロパティの値が<BR>
     * 全て一致するオブジェクトが存在する場合はtrueを返す。<BR>
     * 
     * @param targetObj 対象オブジェクト
     * @param objList オブジェクトのリスト
     * @param uniquePropertyList ユニークプロパティのリスト
     * @return result 重複オブジェクトが存在する場合はtrue、存在しない場合はfalse
     * @throws Exception (NoSuchMethodException, IllegaiArgumentException, InvocationTargetException)
     */
    private boolean isDuplicateObj( Object targetObj, List<Object> objList, List<String> uniquePropertyList) throws Exception {

        boolean result = false;

        if ( objList.size() == 0) {
            // オブジェクトのリストが空の場合
            return false;
        }

        if ( uniquePropertyList.size() == 0) {
            // ユニークプロパティのリストが空の場合
            return false;
        }

        for ( Object obj : objList) {
            // ユニークプロパティ値が重複しているかどうか
            boolean isDuplicate = true;
            for ( String propertyName : uniquePropertyList) {

                Object checkProperty = PropertyUtils.getProperty( targetObj, propertyName);
                Object property = PropertyUtils.getProperty( obj, propertyName);

                if ( property == null) {
                    if ( checkProperty != null) {
                        isDuplicate = false;
                    }
                } else if ( !property.equals( checkProperty)) {
                    isDuplicate = false;
                }
            }
            if ( isDuplicate) {
                // 重複している場合
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * カスタムプロパティ解析クラスを追加する
     * 
     * @param parser 追加するカスタムプロパティ解析クラス
     */
    public void addPropertyParser( SheetToJavaPropertyParser parser) {
        customPropertyParsers.add( parser);
    }

    /**
     * すべてのカスタムプロパティ解析クラスを削除する
     */
    public void clearPropertyParsers() {
        customPropertyParsers.clear();
    }

    /**
     * 処理リスナの追加
     * 
     * @param listener 追加するSheetToJavaListenerリスナ
     */
    public void addSheetToJavaListener( SheetToJavaListener listener) {
        sheetToJavaListeners.add( listener);
    }

    /**
     * 処理リスナのクリア
     */
    public void clearSheetToJavaListeners() {
        sheetToJavaListeners.clear();
    }
}
