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

import java.math.BigDecimal;
import java.util.Date;

/**
 * サンプルユーザ組織マップエンティティクラス
 * 
 * @since 1.0
 */
public class SampleUserOrgMap {

    /**
     * ユーザ組織マップId
     */
    private long userOrgMapId;

    /**
     * ユーザコード
     */
    private String userCode = null;

    /**
     * 組織コード
     */
    private String orgCode = null;

    /**
     * 所属日
     */
    private Date startDate = null;

    /**
     * 標準勤務時間
     */
    private BigDecimal stdWorkHours = null;

    /**
     * 無効フラグ
     */
    private boolean invalidFlag = false;

    /**
     * ユーザ組織マップIdを取得する
     * 
     * @return ユーザ組織マップId
     */
    public long getUserOrgMapId() {
        return userOrgMapId;
    }

    /**
     * ユーザ組織マップIdを設定する
     * 
     * @param userOrgMapId ユーザ組織マップId
     */
    public void setUserOrgMapId( long userOrgMapId) {
        this.userOrgMapId = userOrgMapId;
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
     * 所属日を取得する
     * 
     * @return 所属日
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * 所属日を設定する
     * 
     * @param startDate 所属日
     */
    public void setStartDate( Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 標準勤務時間を取得する
     * 
     * @return 標準勤務時間
     */
    public BigDecimal getStdWorkHours() {
        return stdWorkHours;
    }

    /**
     * 標準勤務時間を設定する
     * 
     * @param stdWorkHours 標準勤務時間
     */
    public void setStdWorkHours( BigDecimal stdWorkHours) {
        this.stdWorkHours = stdWorkHours;
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
        strBuild.append( "orgCode = ");
        strBuild.append( orgCode);
        strBuild.append( "\n");
        strBuild.append( "startDate = ");
        strBuild.append( startDate);
        strBuild.append( "\n");
        strBuild.append( "stdWorkHours = ");
        strBuild.append( stdWorkHours);
        strBuild.append( "\n");
        strBuild.append( "invalidFlag = ");
        strBuild.append( invalidFlag);

        return strBuild.toString();
    }
}
