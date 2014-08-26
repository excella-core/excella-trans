/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: TestChildEntity.java 2 2009-06-22 04:48:53Z yuta-takahashi $
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

/**
 * テスト用子エンティティ 
 *
 * @since 1.0
 */
public class TestChildEntity {

    /**
     * 文字列プロパティ1
     */
    private String childPropertyStr1 = null;
    
    /**
     * 整数プロパティ1
     */
    private Integer childPropertyInt1 = null;
    
    public String getChildPropertyStr1() {
        return childPropertyStr1;
    }
    public void setChildPropertyStr1( String childPropertyStr1) {
        this.childPropertyStr1 = childPropertyStr1;
    }
    public Integer getChildPropertyInt1() {
        return childPropertyInt1;
    }
    public void setChildPropertyInt1( Integer childPropertyInt1) {
        this.childPropertyInt1 = childPropertyInt1;
    }
}
