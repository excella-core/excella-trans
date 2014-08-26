/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: AllTestSuite.java 2 2009-06-22 04:48:53Z yuta-takahashi $
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
