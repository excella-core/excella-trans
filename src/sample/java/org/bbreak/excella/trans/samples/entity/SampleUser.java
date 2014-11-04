/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: SampleUser.java 20 2009-06-24 08:01:33Z yuta-takahashi $
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
