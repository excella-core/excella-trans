/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: SheetToJavaListener.java 56 2009-11-17 09:19:55Z akira-yokoi $
 * $Revision: 56 $
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
package org.bbreak.excella.trans.tag.sheet2java;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.bbreak.excella.core.exception.ParseException;

/**
 * SheetToJava用プロパティ設定処理リスナ
 * プロパティ設定の前後に呼ばれるメソッドを持ち、プロパティ設定前に値の検証処理を
 * 行いたい場合に利用する。
 * 
 * @since 1.1
 */
public interface SheetToJavaListener {

    /**
     * 行処理前に任意の処理を差し込む
     * falseを返すと対象行は処理対象外とみなし、スキップする
     * 
     * @param row 行
     * @return true=処理対象、false=処理対象外
     */
    boolean preProcessRow( Row row) throws ParseException;
    
    /**
     * 行処理後に任意の処理を差し込む
     * falseを返すと対象行の結果は無効とみなし、スキップする
     * 
     * @param row 行
     * @param obj 行を処理した結果のオブジェクト
     * @return true=処理対象、false=処理対象外
     */
    boolean postProcessRow( Row row, Object obj) throws ParseException;
    
    /**
     * プロパティ設定前に任意の処理を差し込む
     * 
     * @param valueCell 値の入ったセル
     * @param obj プロパティ設定対象オブジェクト
     * @param propertyName プロパティ名
     * @param value 値
     * @throws ParseException 検証エラーの場合
     */
    void preSetProperty( Cell valueCell, Object obj, String propertyName, Object value) throws ParseException;
    
    /**
     * プロパティ設定前に任意の処理を差し込む
     * 
     * @param valueCell 値の入ったセル
     * @param obj プロパティ設定対象オブジェクト
     * @param propertyName プロパティ名
     * @param value 値
     * @throws ParseException 検証エラーの場合
     */
    void postSetProperty( Cell valueCell, Object obj, String propertyName, Object value) throws ParseException;
}
