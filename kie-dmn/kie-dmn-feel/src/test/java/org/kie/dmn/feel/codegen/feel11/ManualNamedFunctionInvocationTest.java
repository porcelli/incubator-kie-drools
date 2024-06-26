/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.dmn.feel.codegen.feel11;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.kie.dmn.feel.lang.EvaluationContext;
import org.kie.dmn.feel.lang.impl.NamedParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class ManualNamedFunctionInvocationTest {
    
    public static final Logger LOG = LoggerFactory.getLogger(ManualNamedFunctionInvocationTest.class);

    static class NamedFunctionExample implements CompiledFEELExpression {
        static final java.math.BigDecimal K_1 = new java.math.BigDecimal(2, java.math.MathContext.DECIMAL128);
        static final String K_s = "FOOBAR";

        /**   FEEL: substring( start position: 2, string: "FOOBAR" )  */
        @Override
        public Object apply(EvaluationContext feelExprCtx) {
            return CompiledFEELSupport.invoke(
                    feelExprCtx,
                    feelExprCtx.getValue("substring"),
                    Arrays.asList(new NamedParameter("start position", K_1),
                                  new NamedParameter("string", K_s)));
        }

    }

    @Test
    void manualContext() {
        CompiledFEELExpression compiledExpression = new NamedFunctionExample();
        LOG.debug("{}", compiledExpression);

        EvaluationContext emptyContext = CodegenTestUtil.newEmptyEvaluationContext();
        Object result = compiledExpression.apply(emptyContext);
        LOG.debug("{}", result);

        assertThat(result).isEqualTo("OOBAR");
    }

}
