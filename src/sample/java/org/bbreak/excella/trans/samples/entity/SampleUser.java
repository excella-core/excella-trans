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
 * サンプルユーザエンティティクラス
 * 
 * @since 1.0
 */
public class SampleUser {

    /**
     * ユーザId
     */
    private long userId;

    /**
     * ユーザコード
     */
    private String userCode = null;

    /**
     * ユーザ名
     */
    private String userName = null;

    /**
     * 会社コード
     */
    private String companyCode = null;

    /**
     * 無効フラグ
     */
    private boolean invalidFlag = false;

    /**
     * ユーザIdを取得する
     * 
     * @return ユーザId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * ユーザIdを設定する
     * 
     * @param userId ユーザId
     */
    public void setUserId( long userId) {
        this.userId = userId;
    }

    /**
     * ユーザコードを取得する
     * 
     * @return ユーザコード
     */
    public String getUserCode() {
        return userCode;
    }

    /**
     * ユーザコードを設定する
     * 
     * @param userCode ユーザコード
     */
    public void setUserCode( String userCode) {
        this.userCode = userCode;
    }

    /**
     * ユーザ名を取得する
     * 
     * @return ユーザ名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * ユーザ名を設定する
     * 
     * @param userName ユーザ名
     */
    public void setUserName( String userName) {
        this.userName = userName;
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
     * 会社コードを設定する
     * 
     * @param companyCode 会社コード
     */
    public void setCompanyCode( String companyCode) {
        this.companyCode = companyCode;
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
        strBuild.append( "userCode = ");
        strBuild.append( userCode);
        strBuild.append( "\n");
        strBuild.append( "userName = ");
        strBuild.append( userName);
        strBuild.append( "\n");
        strBuild.append( "companyCode = ");
        strBuild.append( companyCode);
        strBuild.append( "\n");
        strBuild.append( "invalidFlag = ");
        strBuild.append( invalidFlag);

        return strBuild.toString();
    }
}
