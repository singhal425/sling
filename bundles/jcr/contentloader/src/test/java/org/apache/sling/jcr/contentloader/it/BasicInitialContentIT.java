/*
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
package org.apache.sling.jcr.contentloader.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.commons.testing.junit.Retry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.tinybundles.core.TinyBundle;
import org.osgi.framework.Bundle;

/** Basic test of a bundle that provides initial content */
@RunWith(PaxExam.class)
public class BasicInitialContentIT extends ContentBundleTestBase {
    
    protected TinyBundle setupTestBundle(TinyBundle b) throws IOException {
        b.set(SLING_INITIAL_CONTENT_HEADER, DEFAULT_PATH_IN_BUNDLE + ";path:=" + contentRootPath);
        addContent(b, DEFAULT_PATH_IN_BUNDLE, "basic-content.json");
        addContent(b, DEFAULT_PATH_IN_BUNDLE, "simple-folder/test1.txt");
        addContent(b, DEFAULT_PATH_IN_BUNDLE, "folder-with-descriptor.json");
        addContent(b, DEFAULT_PATH_IN_BUNDLE, "folder-with-descriptor/test2.txt");
        return b;
    }
    
    @Test
    @Retry(intervalMsec=RETRY_INTERVAL, timeoutMsec=RETRY_TIMEOUT)
    public void bundleStarted() {
        final Bundle b = PaxExamUtilities.findBundle(bundleContext, bundleSymbolicName);
        assertNotNull("Expecting bundle to be found:" + bundleSymbolicName, b);
        assertEquals("Expecting bundle to be active:" + bundleSymbolicName, Bundle.ACTIVE, b.getState());
    }
    
    @Test
    @Retry(intervalMsec=RETRY_INTERVAL, timeoutMsec=RETRY_TIMEOUT)
    public void initialContentInstalled() throws RepositoryException {
        final String testNodePath = contentRootPath + "/basic-content/test-node"; 
        assertTrue("Expecting initial content to be installed", session.itemExists(testNodePath)); 
        assertEquals("Expecting foo=bar", "bar", session.getNode(testNodePath).getProperty("foo").getString()); 
    }

    @Test
    @Retry(intervalMsec=RETRY_INTERVAL, timeoutMsec=RETRY_TIMEOUT)
    public void folderWithoutDescriptor() throws RepositoryException {
        final String folderPath = contentRootPath + "/simple-folder"; 
        assertTrue("folder node " + folderPath + " exists", session.itemExists(folderPath)); 
        assertEquals("folder has node type 'sling:Folder'", "sling:Folder", session.getNode(folderPath).getPrimaryNodeType().getName()); 

        final String filePath = contentRootPath + "/simple-folder/test1.txt"; 
        assertTrue("file node " + filePath + " exists", session.itemExists(filePath)); 
        assertEquals("file has node type 'nt:file'", "nt:file", session.getNode(filePath).getPrimaryNodeType().getName()); 
    }

    @Test
    @Retry(intervalMsec=RETRY_INTERVAL, timeoutMsec=RETRY_TIMEOUT)
    public void folderWithDescriptor() throws RepositoryException {
        final String folderPath = contentRootPath + "/folder-with-descriptor"; 
        assertTrue("folder node " + folderPath + " exists", session.itemExists(folderPath)); 
        assertEquals("folder has node type 'sling:OrderedFolder'", "sling:OrderedFolder", session.getNode(folderPath).getPrimaryNodeType().getName()); 

        final String filePath = contentRootPath + "/folder-with-descriptor/test2.txt"; 
        assertTrue("file node " + filePath + " exists", session.itemExists(filePath)); 
        assertEquals("file has node type 'nt:file'", "nt:file", session.getNode(filePath).getPrimaryNodeType().getName()); 
    }

}
