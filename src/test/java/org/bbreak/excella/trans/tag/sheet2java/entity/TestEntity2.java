/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: TestEntity2.java 2 2009-06-22 04:48:53Z yuta-takahashi $
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
package org.bbreak.excella.trans.tag.sheet2java.entity;

import java.util.Date;

/**
 * テスト用エンティティ
 *
 * @since 1.0
 */
public class TestEntity2 {

    /**
     * 文字列プロパティ2
     */
    private String propertyStr2 = null;
    
    /**
     * 整数プロパティ2
     */
    private Integer propertyInt2 = null;
    
    /**
     * 日付プロパティ2
     */
    private Date propertyDate2 = null;
    
    public String getPropertyStr2() {
        return propertyStr2;
    }
    public void setPropertyStr2( String propertyStr2) {
        this.propertyStr2 = propertyStr2;
    }
    public Integer getPropertyInt2() {
        return propertyInt2;
    }
    public void setPropertyInt2( Integer propertyInt2) {
        this.propertyInt2 = propertyInt2;
    }
    public Date getPropertyDate2() {
        return propertyDate2;
    }
    public void setPropertyDate2( Date propertyDate2) {
        this.propertyDate2 = propertyDate2;
    }
}
