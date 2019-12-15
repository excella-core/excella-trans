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
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.core.tag.TagParser;
import org.bbreak.excella.core.util.PoiUtil;
import org.bbreak.excella.core.util.TagUtil;
import org.bbreak.excella.trans.tag.sheet2java.model.SheetToJavaSettingInfo;

/**
 * シートの明細情報を解析するパーサ
 * 
 * @since 1.0
 */
public class SheetToJavaSettingParser extends TagParser<List<SheetToJavaSettingInfo>> {

    /**
     * デフォルトタグ
     */
    public static final String DEFAULT_TAG = "@SheetToJavaSetting";

    /**
     * データ開始行の調整パラメータ
     */
    protected static final String PARAM_DATA_ROW_FROM = "DataRowFrom";

    /**
     * データ終了行の調整パラメータ
     */
    protected static final String PARAM_DATA_ROW_TO = "DataRowTo";

    /**
     * デフォルトデータ開始行調整値
     */
    protected static final int DEFAULT_DATA_ROW_FROM_ADJUST = 2;

    /**
     * ユニークプロパティのマーク
     */
    protected static final String UNIQUE_PROPERTY_MARK = "○";

    /**
     * コンストラクタ
     */
    public SheetToJavaSettingParser() {
        super( DEFAULT_TAG);
    }

    /**
     * コンストラクタ
     * 
     * @param tag タグ
     */
    public SheetToJavaSettingParser( String tag) {
        super( tag);
    }

    /**
     * パース処理
     * 
     * @param sheet 対象シート
     * @param tagCell タグが定義されたセル
     * @param data TransProcessorのprocessBook, processSheetメソッドで<BR> 
     *              引数を渡した場合にTagParser.parseメソッドまで引き継がれる処理データ<BR>
     * @return パース結果
     * @throws ParseException パース例外
     */
    @Override
    public List<SheetToJavaSettingInfo> parse( Sheet sheet, Cell tagCell, Object data) throws ParseException {

        // タグのインデックス
        int tagRowIdx = tagCell.getRowIndex();
        int tagColIdx = tagCell.getColumnIndex();

        // データ行インデックス
        int valueRowFromIdx;
        int valueRowToIdx = sheet.getLastRowNum();

        try {
            Map<String, String> paramDef = TagUtil.getParams( tagCell.getStringCellValue());

            // データ開始行の調整
            valueRowFromIdx = TagUtil.adjustValue( tagRowIdx, paramDef, PARAM_DATA_ROW_FROM, DEFAULT_DATA_ROW_FROM_ADJUST);
            if ( valueRowFromIdx < 0 || valueRowFromIdx > sheet.getLastRowNum()) {
                throw new ParseException( tagCell, "パラメータ値不正：" + PARAM_DATA_ROW_FROM);
            }

            // データ終了行の調整
            valueRowToIdx = TagUtil.adjustValue( tagRowIdx, paramDef, PARAM_DATA_ROW_TO, valueRowToIdx - tagRowIdx);
            if ( valueRowToIdx > sheet.getLastRowNum() || valueRowToIdx < 0) {
                throw new ParseException( tagCell, "パラメータ値不正：" + PARAM_DATA_ROW_TO);
            }
            
            // データ開始行と終了行の関係チェック
            if ( valueRowFromIdx > valueRowToIdx) {
                throw new ParseException( tagCell, "パラメータ値不正：" + PARAM_DATA_ROW_FROM + "," + PARAM_DATA_ROW_TO);
            }

        } catch ( Exception e) {
            if ( e instanceof ParseException) {
                throw ( ParseException) e;
            } else {
                throw new ParseException( tagCell, e);
            }
        }

        List<SheetToJavaSettingInfo> sheetSettingInfoList = new ArrayList<SheetToJavaSettingInfo>();

        // シート名列インデックス
        int sheetNameColIdx = tagColIdx++;
        // 値列インデックス
        int valueColIdx = tagColIdx++;
        // 対象エンティティ列インデックス
        int classColIdx = tagColIdx++;
        // 対象プロパティ列インデックス
        int propertyNameColIdx = tagColIdx++;
        // 重複付加列インデックス
        int uniqueColIdx = tagColIdx++;

        // シート名チェック用にワークブックを取得
        Workbook workbook = sheet.getWorkbook();

        // シート情報の読み込み
        for ( int rowNum = valueRowFromIdx; rowNum <= valueRowToIdx; rowNum++) {
            Row row = sheet.getRow( rowNum);
            if ( row != null) {
                // セルの取得
                Cell sheetNameCell = row.getCell( sheetNameColIdx);
                Cell valueCell = row.getCell( valueColIdx);
                Cell classCell = row.getCell( classColIdx);
                Cell propertyNameCell = row.getCell( propertyNameColIdx);
                Cell uniqueCell = row.getCell( uniqueColIdx);

                // 必須チェック
                if ( (sheetNameCell == null) && (valueCell == null) && (classCell == null) 
                        && (propertyNameCell == null) && (uniqueCell == null)) {
                    // 対象セルがすべてnullの場合
                    continue;

                } else if ((sheetNameCell == null) 
                        || (sheetNameCell.getStringCellValue() == null)
                        || ("".equals( sheetNameCell.getStringCellValue()))) {
                    // シート名が設定されていない場合
                    continue;

                } else {
                    // それ以外の場合
                    Cell requiredErrorCell = null;
                    if ( classCell == null) {
                        // 対象クラスセルがnullの場合
                        requiredErrorCell = row.createCell( classColIdx);
                    }

                    // 必須エラー時は例外を投げる
                    if ( requiredErrorCell != null) {
                        throw new ParseException( requiredErrorCell, "必須セルがnullです");
                    }
                }

                // 解析シートの詳細情報
                SheetToJavaSettingInfo settingInfo = new SheetToJavaSettingInfo();

                // シート名の存在チェック
                String sheetName = sheetNameCell.getStringCellValue();
                if ( workbook.getSheet( sheetName) == null) {
                    throw new ParseException( sheetNameCell, "シート[" + sheetName + "]は存在しません");
                }

                // シート名を設定
                settingInfo.setSheetName( sheetName);
                settingInfo.setSheetNameCell(  sheetNameCell);

                // 対象エンティティのクラスを設定
                try {
                    settingInfo.setClazz( Class.forName( classCell.getStringCellValue()));
                    settingInfo.setClazzCell( classCell);
                } catch ( ClassNotFoundException e) {
                    throw new ParseException( classCell, e);
                }

                // 値
                Object value = PoiUtil.getCellValue( valueCell);
                settingInfo.setValueCell( valueCell);

                // 値がタグかどうか
                boolean isValueTag = false;
                // 値が論理名タグかどうか
                boolean isValueLogicalNameTag = false;
                if ( value instanceof String) {
                    // 文字列の場合
                    String valueStr = ( String) value;
                    if ( (valueStr).startsWith( SheetToJavaExecuter.TAG_PREFIX)) {
                        // タグの場合
                        isValueTag = true;
                        if ( (valueStr).startsWith( SheetToJavaExecuter.TAG_LOGICAL_NAME_PREFIX)) {
                            // 論理名タグの場合
                            isValueLogicalNameTag = true;
                        }
                    }
                }

                if ( !isValueTag || isValueLogicalNameTag) {
                    // 値がタグ以外または論理名タグの場合

                    // 対象プロパティセルの必須チェック
                    Cell requiredErrorCell = null;
                    if ( propertyNameCell == null) {
                        requiredErrorCell = row.createCell( propertyNameColIdx);
                    }
                    if ( requiredErrorCell != null) {
                        throw new ParseException( requiredErrorCell, "必須セルがnullです");
                    }

                    // 対象プロパティの設定
                    settingInfo.setPropertyName( propertyNameCell.getStringCellValue());
                    settingInfo.setPropertyNameCell(  propertyNameCell);

                    // プロパティ存在チェック
                    Class<?> propertyClass = null;
                    try {
                        Object obj = settingInfo.getClazz().newInstance();
                        propertyClass = PropertyUtils.getPropertyType( obj, settingInfo.getPropertyName());
                    } catch ( Exception e) {
                        throw new ParseException( propertyNameCell, e);
                    }
                    if ( propertyClass == null) {
                        throw new ParseException( propertyNameCell, "プロパティ不正:" + settingInfo.getPropertyName());
                    }

                    // 重複不可の設定
                    if ( uniqueCell != null) {
                        if ( uniqueCell.getStringCellValue() != null && uniqueCell.getStringCellValue().equals( UNIQUE_PROPERTY_MARK)) {
                            settingInfo.setUnique( true);
                            settingInfo.setUniqueCell( uniqueCell);
                        }
                    }
                } else {
                    // カスタムプロパティタグの場合

                    // パラメータ定義が正しいかチェック
                    try {
                        TagUtil.getParams( ( String) value);
                    } catch ( Exception e) {
                        throw new ParseException( valueCell, e);
                    }
                }

                // 型変換するかどうか
                boolean checkTypeFlag = false;
                if ( value instanceof String) {
                    if ( !isValueTag) {
                        // タグ以外の場合
                        checkTypeFlag = true;
                    }
                } else {
                    // 文字列以外の場合
                    if ( value != null) {
                        // null以外の場合
                        checkTypeFlag = true;
                    }
                }

                // 値の設定
                if ( checkTypeFlag) {
                    // 値をセットする際に型変換する
                    Object obj;
                    try {
                        obj = settingInfo.getClazz().newInstance();
                        Class<?> propertyClass = PropertyUtils.getPropertyType( obj, settingInfo.getPropertyName());
                        value = PoiUtil.getCellValue( valueCell, propertyClass);
                    } catch ( Exception e) {
                        throw new ParseException( valueCell, e);
                    }
                }
                settingInfo.setValue( value);

                // 結果リストに格納する
                sheetSettingInfoList.add( settingInfo);
            }
        }
        return sheetSettingInfoList;
    }
}
