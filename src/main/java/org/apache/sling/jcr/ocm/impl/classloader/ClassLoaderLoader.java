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
package org.apache.sling.jcr.ocm.impl.classloader;

/**
 * The <code>ClassLoaderLoader</code> TODO
 */
public class ClassLoaderLoader implements Loader {

    private ClassLoader classLoader;

    ClassLoaderLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * @see org.apache.sling.jcr.resource.internal.mapping.classloader.Loader#loadClass(java.lang.String)
     */
    public Class loadClass(String name) throws ClassNotFoundException {
        return this.classLoader.loadClass(name);
    }

    /**
     * @see org.apache.sling.jcr.resource.internal.mapping.classloader.Loader#getLoader()
     */
    public Object getLoader() {
        return this.classLoader;
    }
}
