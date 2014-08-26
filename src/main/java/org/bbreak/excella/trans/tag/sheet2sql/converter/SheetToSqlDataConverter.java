/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: SheetToSqlDataConverter.java 40 2009-11-12 04:51:10Z akira-yokoi $
 * $Revision: 40 $
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
package org.bbreak.excella.trans.tag.sheet2sql.converter;

import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.trans.tag.sheet2sql.model.SheetToSqlSettingInfo;

/**
 * SheetToSqlで使用するデータコンバータ
 *
 * @since 1.0
 */
public interface SheetToSqlDataConverter {

    /**
     * オブジェクトをデータ型に基づき文字列にコンバートし、返却する
     * 
     * @param object オブジェクト
     * @param dataType データ型
     * @param settingInfo SQL変換設定情報
     * @return コンバートされた文字列
     * @throws ParseException パース例外
     */
    String convert( Object object, String dataType, SheetToSqlSettingInfo settingInfo) throws ParseException;
}
