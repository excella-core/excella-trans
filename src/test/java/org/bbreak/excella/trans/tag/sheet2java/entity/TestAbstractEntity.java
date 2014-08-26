/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: TestAbstractEntity.java 2 2009-06-22 04:48:53Z yuta-takahashi $
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
public abstract class TestAbstractEntity {

    /**
     * 文字列プロパティ1
     */
    private String propertyStr1 = null;
    
    /**
     * 整数プロパティ1
     */
    private Integer propertyInt1 = null;

    /**
     * 日付プロパティ1
     */
    private Date propertyDate1 = null;
    
    public String getPropertyStr1() {
        return propertyStr1;
    }
    public void setPropertyStr1( String propertyStr1) {
        this.propertyStr1 = propertyStr1;
    }
    public Integer getPropertyInt1() {
        return propertyInt1;
    }
    public void setPropertyInt1( Integer propertyInt1) {
        this.propertyInt1 = propertyInt1;
    }
    public Date getPropertyDate1() {
        return propertyDate1;
    }
    public void setPropertyDate1( Date propertyDate1) {
        this.propertyDate1 = propertyDate1;
    }
    
}
