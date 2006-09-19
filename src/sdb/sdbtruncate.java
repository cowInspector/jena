/*
 * (c) Copyright 2006 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package sdb;

import java.util.List;

import sdb.cmd.CmdArgsDB;

import com.hp.hpl.jena.query.util.Utils;

/** Format an SDB database.  Destroys all existing data permanently.
 *  Ignores -dbName argument in favour of the command line positional parameter. 
 * @author Andy Seaborne
 * @version $Id$
 */ 

public class sdbtruncate extends CmdArgsDB
{
    public static void main (String [] argv)
    {
        new sdbtruncate(argv).mainAndExit() ;
    }

    private String dbToZap = null ;
    
    protected sdbtruncate(String[] args)
    {
        super(args);
        
    }
    
    @Override
    protected String getCommandName() { return Utils.className(this) ; }
    
    @Override
    protected String getSummary()  { return Utils.className(this)+" --sdb <SPEC> <NAME>" ; }
    
    @Override
    protected void processModulesAndArgs()
    {
        if ( getNumPositional() == 1 )
        {
            String dbToZap = getPositionalArg(0) ;
            getModStore().setDbName(dbToZap) ;
        }
        else
            cmdError("Must give the database name explicitly") ;
        
//        // Safety check
//        if ( !contains("dbName") )
//            cmdError("Must give the name of the database") ;
    }
    
    @Override
    protected void execCmd(List<String> args)
    {
        getModStore().getStore().getTableFormatter().truncate() ;
    }
}

/*
 * (c) Copyright 2006 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */