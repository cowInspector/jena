/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.riot.stream;

import java.io.File ;

import org.apache.jena.riot.TypedInputStream2 ;
import org.apache.jena.riot.WebReader2 ;
import org.apache.jena.riot.stream.LocatorFile2 ;
import org.apache.jena.riot.stream.LocatorURL2 ;
import org.apache.jena.riot.stream.StreamManager ;
import org.junit.AfterClass ;
import org.junit.BeforeClass ;
import org.junit.Test ;
import org.openjena.atlas.junit.BaseTest ;
import org.openjena.riot.RiotNotFoundException ;

import com.hp.hpl.jena.rdf.model.Model ;
import com.hp.hpl.jena.rdf.model.ModelFactory ;
import com.hp.hpl.jena.sparql.util.Context ;

public class TestStreamManager extends BaseTest
{
    private static final String directory = "testing/RIOT/StreamManager" ;
    private static final String absDirectory = new File(directory).getAbsolutePath() ;

    private static Context context = new Context() ;
    
    private static StreamManager streamMgr ;
    private static StreamManager streamMgrStd ;
    private static Object streamManagerContextValue ;
    
    @BeforeClass static public void beforeClass()
    { 
        streamMgrStd = StreamManager.get() ;
        streamMgr = new StreamManager() ;
        streamMgr.addLocator(new LocatorFile2()) ;
        streamMgr.addLocator(new LocatorFile2(directory)) ;
        streamMgr.addLocator(new LocatorURL2()) ;
        
        streamManagerContextValue = context.get(WebReader2.streamManagerSymbol) ;
        context.put(WebReader2.streamManagerSymbol, streamMgr) ;
    }
    
    @AfterClass static public void afterClass()
    { 
        StreamManager.setGlobal(streamMgrStd) ;
        if ( streamManagerContextValue == null )
            context.remove(WebReader2.streamManagerSymbol) ;
        else
            context.put(WebReader2.streamManagerSymbol, streamManagerContextValue) ;
    }
    
    @Test public void fm_open_01() { open(directory+"/D.ttl", context) ; }
    @Test public void fm_open_02() { open("D.ttl", context) ; }

    @Test public void fm_open_03() { open("file:"+directory+"/D.ttl", context) ; }
    @Test public void fm_open_04() { open("file:D.ttl", context) ; }
    
    @Test public void fm_open_05() { open(absDirectory+"/D.ttl", context) ; }
    @Test public void fm_open_06() { open("file://"+absDirectory+"/D.ttl", context) ; }
    
    @Test (expected=RiotNotFoundException.class)
    public void fm_open_10() { open("nosuchfile", null) ; }
    
    @Test public void fm_read_01() { read("D.nt") ; }
    @Test public void fm_read_02() { read("D.ttl") ; }
    @Test public void fm_read_03() { read("D.rdf") ; }
    @Test public void fm_read_04() { read("D.json") ; }

    @Test public void fm_read_11() { read("file:D.nt") ; }
    @Test public void fm_read_12() { read("file:D.ttl") ; }
    @Test public void fm_read_13() { read("file:D.rdf") ; }
    @Test public void fm_read_14() { read("file:D.json") ; }
    
    // TriG
    // NQuads
    
    private static void open(String dataName, Context context)
    {
        TypedInputStream2 in ;
        if ( context != null )
            in = WebReader2.open(dataName, context) ;
        else
            in = WebReader2.open(dataName) ;
        assertNotNull(in) ;
        in.close() ;
    }
    
    private static void read(String dataName)
    {
        StreamManager.setGlobal(streamMgr) ;
        Model m = ModelFactory.createDefaultModel() ;
        WebReader2.read(m, dataName) ;
        assertTrue(m.size() != 0 ) ;
        StreamManager.setGlobal(streamMgrStd) ;
    }
}

