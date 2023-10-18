package org.bbreak.excella.trans.listener;

import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.BookData;
import org.bbreak.excella.core.exception.ParseException;

/**
 * ブック解析後に差し込む処理を示すインタフェース
 * 
 * @since 2.1
 */
@FunctionalInterface
public interface PostBookParseListener {

    /**
     * ブック解析後に呼び出されるメソッド
     * 
     * @param workbook 対象ブック
     * @param bookData 解析結果のデータ
     * @throws ParseException 解析処理を中断しなければならない問題が生じた場合
     */
    void postBookParse( Workbook workbook, BookData bookData) throws ParseException;
}
