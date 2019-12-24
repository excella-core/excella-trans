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

package org.bbreak.excella.trans;

import org.bbreak.excella.trans.listener.ListenerTestSuite;
import org.bbreak.excella.trans.processor.ProcessorTestSuite;
import org.bbreak.excella.trans.tag.sheet2java.TagSheet2JavaTestSuite;
import org.bbreak.excella.trans.tag.sheet2java.model.TagSheet2JavaModelTestSuite;
import org.bbreak.excella.trans.tag.sheet2sql.TagSheet2SqlTestSuite;
import org.bbreak.excella.trans.tag.sheet2sql.converter.TagSheet2SqlConverterTestSuite;
import org.bbreak.excella.trans.tag.sheet2sql.model.TagSheet2SqlModelTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 全テスト実行用テストスイート
 * 
 * @since 1.0
 */
@RunWith(Suite.class)
@SuiteClasses({
    ListenerTestSuite.class,
    ProcessorTestSuite.class,
    TagSheet2JavaModelTestSuite.class,
    TagSheet2JavaTestSuite.class,
    TagSheet2SqlModelTestSuite.class,
    TagSheet2SqlTestSuite.class,
    TagSheet2SqlConverterTestSuite.class
})
public class AllTestSuite {
}
