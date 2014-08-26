/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: TransProcessListener.java 2 2009-06-22 04:48:53Z yuta-takahashi $
 * $Revision: 2 $
 *
 * This file is part of ExCella Trans.
 *
 * ExCella Trans is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * ExCella Trans is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the COPYING.LESSER file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with ExCella Trans.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0-standalone.html>
 * for a copy of the LGPLv3 License.
 *
 ************************************************************************/
package org.bbreak.excella.trans.listener;

import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.BookData;
import org.bbreak.excella.core.exception.ParseException;

/**
 * ブック処理前後に呼び出されるイベントリスナのインターフェイス
 * 
 * @since 1.0
 */
public interface TransProcessListener {

    /**
     * ブック処理前に呼び出されるメソッド
     * 
     * @param workbook ワークブック
     * @throws ParseException パース例外
     */
    void preBookParse( Workbook workbook) throws ParseException;

    /**
     * ブック処理後に呼び出されるメソッド
     * 
     * @param workbook ワークブック
     * @param bookData 処理結果のデータ
     * @throws ParseException パース例外
     */
    void postBookParse( Workbook workbook, BookData bookData) throws ParseException;
}
