package org.bbreak.excella.trans.listener;

import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.exception.ParseException;

/**
 * ブック解析前に差し込む処理を示すインタフェース
 * 
 * @since 2.1
 */
@FunctionalInterface
public interface PreBookParseListener {

    /**
     * ブック解析前に呼び出されるメソッド
     * 
     * @param workbook 対象ブック
     * @throws ParseException 解析処理を中断しなければならない問題が生じた場合
     */
    void preBookParse( Workbook workbook) throws ParseException;
}
