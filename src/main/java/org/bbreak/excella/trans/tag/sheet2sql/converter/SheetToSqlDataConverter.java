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
