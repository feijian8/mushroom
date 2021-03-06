/**
   Copyright [2013] [Mushroom]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
/**
 Notice : this source is extracted from Hadoop metric2 package
 and some source code may changed by zavakid
 */
package com.zavakid.mushroom.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.configuration.SubsetConfiguration;
import org.junit.Test;

import com.zavakid.mushroom.filter.TestPatternFilter;

/**
 * @author Hadoop metric2 package's authors
 * @since 0.1
 */
public class TestMetricsBuilderImpl {

    @Test
    public void recordBuilderShouldNoOpIfFiltered() {
        SubsetConfiguration fc = new ConfigBuilder().add("p.exclude", "foo").subset("p");
        MetricsBuilderImpl mb = new MetricsBuilderImpl();
        mb.setRecordFilter(TestPatternFilter.newGlobFilter(fc));
        MetricsRecordBuilderImpl rb = mb.addRecord("foo");
        rb.tag("foo", "", "value").addGauge("g0", "", 1);
        assertEquals("no tags", 0, rb.tags().size());
        assertEquals("no metrics", 0, rb.metrics().size());
        assertNull("null record", rb.getRecord());
        assertEquals("no records", 0, mb.getRecords().size());
    }

    @Test
    public void testPerMetricFiltering() {
        SubsetConfiguration fc = new ConfigBuilder().add("p.exclude", "foo").subset("p");
        MetricsBuilderImpl mb = new MetricsBuilderImpl();
        mb.setMetricFilter(TestPatternFilter.newGlobFilter(fc));
        MetricsRecordBuilderImpl rb = mb.addRecord("foo");
        rb.tag("foo", "", "").addCounter("c0", "", 0).addGauge("foo", "", 1);
        assertEquals("1 tag", 1, rb.tags().size());
        assertEquals("1 metric", 1, rb.metrics().size());
        assertEquals("expect foo tag", "foo", rb.tags().get(0).name());
        assertEquals("expect c0", "c0", rb.metrics().get(0).name());
    }

}
