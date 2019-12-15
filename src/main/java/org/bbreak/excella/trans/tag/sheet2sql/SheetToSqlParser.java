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

package org.bbreak.excella.trans.tag.sheet2sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.core.tag.TagParser;
import org.bbreak.excella.core.util.PoiUtil;
import org.bbreak.excella.core.util.TagUtil;
import org.bbreak.excella.trans.tag.sheet2sql.model.SheetToSqlParseInfo;

/**
 * シート解析の実行情報を解析するパーサ
 * 
 * @since 1.0
 */
public class SheetToSqlParser extends TagParser<List<SheetToSqlParseInfo>> {

    /**
     * デフォルトタグ
     */
    public static final String DEFAULT_TAG = "@SheetToSql";

    /**
     * 対応する設定のタグ名称
     */
    protected static final String PARAM_SETTING_TAG_NAME = "SettingTagName";

    /**
     * デフォルトの設定名称用接尾後
     */
    protected static final String DEFAULT_SETTING_SUFFIX = "Setting";

    /**
     * データ開始行の調整パラメータ
     */
    protected static final String PARAM_DATA_ROW_FROM = "DataRowFrom";

    /**
     * データ終了行の調整パラメータ
     */
    protected static final String PARAM_DATA_ROW_TO = "DataRowTo";

    /**
     * 結果キーパラメータ
     */
    protected static final String PARAM_RESULT_KEY = "ResultKey";

    /**
     * デフォルトデータ開始行調整値
     */
    protected static final int DEFAULT_DATA_ROW_FROM_ADJUST = 2;

    /**
     * コンストラクタ
     */
    public SheetToSqlParser() {
        super( DEFAULT_TAG);
    }

    /**
     * コンストラクタ
     * 
     * @param tag タグ
     */
    public SheetToSqlParser( String tag) {
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
    public List<SheetToSqlParseInfo> parse( Sheet sheet, Cell tagCell, Object data) throws ParseException {

        // タグのインデックス
        int tagRowIdx = tagCell.getRowIndex();
        int tagColIdx = tagCell.getColumnIndex();

        int valueRowFromIdx;
        int valueRowToIdx = sheet.getLastRowNum();

        // 対応する設定タグ名のデフォルト値
        String settingTagName = getTag() + DEFAULT_SETTING_SUFFIX;

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

            // 設定タグ名の取得
            if ( paramDef.containsKey( PARAM_SETTING_TAG_NAME)) {
                settingTagName = paramDef.get( PARAM_SETTING_TAG_NAME);
            }

            // 結果キー
            if ( paramDef.containsKey( PARAM_RESULT_KEY)) {
                // 結果キーが指定されている場合はエラー
                throw new ParseException( tagCell, PARAM_RESULT_KEY + "は指定できないパラメータです");
            }

        } catch ( Exception e) {
            if ( e instanceof ParseException) {
                throw ( ParseException) e;
            } else {
                throw new ParseException( tagCell, e);
            }
        }

        List<SheetToSqlParseInfo> sheetInfoList = new ArrayList<SheetToSqlParseInfo>();

        // シート名列インデックス
        int sheetNameColIdx = tagColIdx++;
        // 論理名行No列インデックス
        int logicalRowColIdx = tagColIdx++;
        // データ開始行No列インデックス
        int dataRowColIdx = tagColIdx;

        // シート名チェック用にワークブックを取得
        Workbook workbook = sheet.getWorkbook();

        // シート情報の読み込み
        for ( int rowNum = valueRowFromIdx; rowNum <= valueRowToIdx; rowNum++) {
            Row row = sheet.getRow( rowNum);
            if ( row != null) {
                // セルの取得
                Cell sheetNameCell = row.getCell( sheetNameColIdx);
                Cell logicalRowNumCell = row.getCell( logicalRowColIdx);
                Cell valueRowNumCell = row.getCell( dataRowColIdx);

                // 必須チェック
                if ( (sheetNameCell == null) && (logicalRowNumCell == null) && (valueRowNumCell == null)) {
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
                    if ( logicalRowNumCell == null) {
                        requiredErrorCell = row.createCell( logicalRowColIdx);
                    } else if ( valueRowNumCell == null) {
                        requiredErrorCell = row.createCell( dataRowColIdx);
                    }

                    // 必須セルがない場合は例外を投げる
                    if ( requiredErrorCell != null) {
                        throw new ParseException( requiredErrorCell, "必須セルがnullです");
                    }
                }

                // 論理名行Noチェック
                int logicalRowNum;
                try {
                    logicalRowNum = ( Integer) PoiUtil.getCellValue( logicalRowNumCell, Integer.class);
                } catch ( Exception e) {
                    throw new ParseException( logicalRowNumCell, e);
                }

                // データ開始行Noチェック
                int valueRowNum;
                try {
                    valueRowNum = ( Integer) PoiUtil.getCellValue( valueRowNumCell, Integer.class);
                } catch ( Exception e) {
                    throw new ParseException( valueRowNumCell, e);
                }

                // シート名存在チェック
                String sheetName = sheetNameCell.getStringCellValue();
                if ( workbook.getSheet( sheetName) == null) {
                    throw new ParseException( sheetNameCell, "シート[" + sheetName + "]は存在しません");
                }

                // シート情報を設定
                SheetToSqlParseInfo sheetInfo = new SheetToSqlParseInfo();
                sheetInfo.setSettingTagName( settingTagName);
                sheetInfo.setSheetName( sheetName);
                sheetInfo.setLogicalNameRowNum( logicalRowNum);
                sheetInfo.setValueRowNum( valueRowNum);

                sheetInfoList.add( sheetInfo);
            }
        }

        return sheetInfoList;
    }
}
