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
