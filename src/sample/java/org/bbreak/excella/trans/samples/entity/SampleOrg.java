/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: SampleOrg.java 20 2009-06-24 08:01:33Z yuta-takahashi $
 * $Revision: 20 $
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
package org.bbreak.excella.trans.samples.entity;

/**
 * サンプル組織エンティティクラス
 * 
 * @since 1.0
 */
public class SampleOrg {

    /**
     * 組織Id
     */
    private long orgId;

    /**
     * 組織コード
     */
    private String orgCode = null;

    /**
     * 組織名
     */
    private String orgName = null;

    /**
     * 会社コード
     */
    private String companyCode = null;

    /**
     * 備考
     */
    private String note = null;

    /**
     * 無効フラグ
     */
    private boolean invalidFlag = false;

    /**
     * 組織Idを取得する
     * 
     * @return 組織Id
     */
    public long getOrgId() {
        return orgId;
    }

    /**
     * 組織Idを設定する
     * 
     * @param orgId 組織Id
     */
    public void setOrgId( long orgId) {
        this.orgId = orgId;
    }

    /**
     * 組織コードを取得する
     * 
     * @return 組織コード
     */
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * 組織コードを設定する
     * 
     * @param orgCode 組織コード
     */
    public void setOrgCode( String orgCode) {
        this.orgCode = orgCode;
    }

    /**
     * 組織名を取得する
     * 
     * @return 組織名
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * 組織名を設定する
     * 
     * @param orgName 組織名
     */
    public void setOrgName( String orgName) {
        this.orgName = orgName;
    }

    /**
     * 会社コードを取得する
     * 
     * @return 会社コード
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * 会社コード
     * 
     * @param companyCode
     */
    public void setCompanyCode( String companyCode) {
        this.companyCode = companyCode;
    }

    /**
     * 備考を取得する
     * 
     * @return 備考
     */
    public String getNote() {
        return note;
    }

    /**
     * 備考を設定する
     * 
     * @param note 備考
     */
    public void setNote( String note) {
        this.note = note;
    }

    /**
     * 無効フラグを取得する
     * 
     * @return 無効フラグ
     */
    public boolean isInvalidFlag() {
        return invalidFlag;
    }

    /**
     * 無効フラグを設定する
     * 
     * @param invalidFlag 無効フラグ
     */
    public void setInvalidFlag( boolean invalidFlag) {
        this.invalidFlag = invalidFlag;
    }

    /**
     * プロパティに設定されている値を表示する
     */
    @Override
    public String toString() {

        StringBuilder strBuild = new StringBuilder();
        strBuild.append( "\n");
        strBuild.append( "[");
        strBuild.append( this.getClass().getSimpleName());
        strBuild.append( "]\n");
        strBuild.append( "orgCode = ");
        strBuild.append( orgCode);
        strBuild.append( "\n");
        strBuild.append( "orgName = ");
        strBuild.append( orgName);
        strBuild.append( "\n");
        strBuild.append( "companyCode = ");
        strBuild.append( companyCode);
        strBuild.append( "\n");
        strBuild.append( "note = ");
        strBuild.append( note);
        strBuild.append( "\n");
        strBuild.append( "invalidFlag = ");
        strBuild.append( invalidFlag);

        return strBuild.toString();
    }
}
