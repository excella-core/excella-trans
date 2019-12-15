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
import org.bbreak.excella.trans.tag.sheet2sql.model.SheetToSqlSettingInfo;

/**
 * シートの明細情報を解析するパーサ
 * 
 * @since 1.0
 */
public class SheetToSqlSettingParser extends TagParser<List<SheetToSqlSettingInfo>> {

    /**
     * デフォルトタグ
     */
    public static final String DEFAULT_TAG = "@SheetToSqlSetting";

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
    public SheetToSqlSettingParser() {
        super( DEFAULT_TAG);
    }

    /**
     * コンストラクタ
     * 
     * @param tag タグ
     */
    public SheetToSqlSettingParser( String tag) {
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
    public List<SheetToSqlSettingInfo> parse( Sheet sheet, Cell tagCell, Object data) throws ParseException {

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
                throw new ParseException( tagCell, "パラメータ値不正：" + PARAM_DATA_ROW_TO);
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

        List<SheetToSqlSettingInfo> sheetSettingInfoList = new ArrayList<SheetToSqlSettingInfo>();

        // シート名列インデックス
        int sheetNameColIdx = tagColIdx++;
        // 値列インデックス
        int valueColIdx = tagColIdx++;
        // 対象テーブル列インデックス
        int tableColIdx = tagColIdx++;
        // 対象カラム列インデックス
        int columnNameColIdx = tagColIdx++;
        // 重複不可列インデックス
        int uniqueColIdx = tagColIdx++;
        // データ型列インデックス
        int dataTypeColIdx = tagColIdx++;

        // シート名チェック用にワークブックを取得
        Workbook workbook = sheet.getWorkbook();

        // シート情報の読み込み
        for ( int rowNum = valueRowFromIdx; rowNum <= valueRowToIdx; rowNum++) {
            Row row = sheet.getRow( rowNum);
            if ( row != null) {
                // セルの取得
                Cell sheetNameCell = row.getCell( sheetNameColIdx);
                Cell valueCell = row.getCell( valueColIdx);
                Cell tableNameCell = row.getCell( tableColIdx);
                Cell columnNameCell = row.getCell( columnNameColIdx);
                Cell uniqueCell = row.getCell( uniqueColIdx);
                Cell dataTypeCell = row.getCell( dataTypeColIdx);

                // 必須チェック
                if ( (sheetNameCell == null) && (valueCell == null) && (tableNameCell == null) 
                        && (columnNameCell == null) && (uniqueCell == null) && (dataTypeCell == null)) {
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
                    if ( tableNameCell == null) {
                        // 対象テーブルセルがnullの場合
                        requiredErrorCell = row.createCell( tableColIdx);
                    } else if ( columnNameCell == null) {
                        // 対象カラムセルがnullの場合
                        requiredErrorCell = row.createCell( columnNameColIdx);
                    }

                    if ( requiredErrorCell != null) {
                        // 必須エラー時は例外を投げる
                        throw new ParseException( requiredErrorCell, "必須セルがnullです");
                    }
                }

                // 解析シートの詳細情報
                SheetToSqlSettingInfo settingInfo = new SheetToSqlSettingInfo();

                // シート名の存在チェック
                String sheetName = sheetNameCell.getStringCellValue();
                if ( workbook.getSheet( sheetName) == null) {
                    throw new ParseException( sheetNameCell, "シート[" + sheetName + "]は存在しません");
                }

                // シート名を設定
                settingInfo.setSheetName( sheetName);
                settingInfo.setSheetNameCell(  sheetNameCell);

                // テーブル名
                settingInfo.setTableName( tableNameCell.getStringCellValue());
                settingInfo.setTableNameCell(  tableNameCell);

                // カラム
                settingInfo.setColumnName( columnNameCell.getStringCellValue());
                settingInfo.setColumnNameCell(  columnNameCell);

                // 値
                if ( valueCell != null) {
                    Object value = PoiUtil.getCellValue( valueCell);
                    settingInfo.setValue( value);
                    settingInfo.setValueCell(  valueCell);
                }

                // 重複不可
                if ( uniqueCell != null) {
                    if ( uniqueCell.getStringCellValue() != null && uniqueCell.getStringCellValue().equals( UNIQUE_PROPERTY_MARK)) {
                        settingInfo.setUnique( true);
                        settingInfo.setUniqueCell( uniqueCell);
                    }
                }

                // データ型
                if ( dataTypeCell != null) {
                    settingInfo.setDataType( dataTypeCell.getStringCellValue());
                    settingInfo.setDataTypeCell(  dataTypeCell);
                }

                // 結果リストに格納する
                sheetSettingInfoList.add( settingInfo);
            }
        }
        return sheetSettingInfoList;
    }
}
