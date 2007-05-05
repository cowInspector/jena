/*
 * (c) Copyright 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.engine.iterator;

import java.util.Collection;
import java.util.Iterator ;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.engine.ExecutionContext;
import com.hp.hpl.jena.sparql.engine.QueryIterator;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.sparql.engine.binding.BindingBase;
import com.hp.hpl.jena.sparql.serializer.SerializationContext;
import com.hp.hpl.jena.sparql.util.IndentedWriter;
import com.hp.hpl.jena.sparql.util.PrintUtils;
import com.hp.hpl.jena.sparql.util.Utils;


public class QueryIterProject extends QueryIterConvert
{
    Collection projectionVars ;

    public QueryIterProject(QueryIterator input, Collection vars, ExecutionContext qCxt)
    {
        super(input, new Projection(vars), qCxt) ;
        Var.checkVarList(vars) ;
        projectionVars = vars ;
    }

    public Collection getProjectionVars()   { return projectionVars ; }

    protected void releaseResources()
    {}

    protected void details(IndentedWriter out, SerializationContext sCxt)
    {
        out.print(Utils.className(this)) ;
        out.print(" ") ;
        PrintUtils.printList(out, projectionVars) ;
    }
    
    static
    class Projection implements QueryIterConvert.Converter
    {
        Collection projectionVars ; 

        Projection(Collection vars) { projectionVars = vars ; }

        public Binding convert(Binding bind)
        {
            // Non-copying version
            Binding bind2 = new BindingProject(projectionVars, bind) ;
            return bind2 ;
        }
    }

    static
    class BindingProject extends BindingBase
    {
        Binding binding ;
        Collection projectionVars ; 

        public BindingProject(Collection vars, Binding bind)
        { 
            super(null) ;
            binding = bind ;
            this.projectionVars = vars ;
        }

        protected void add1(Var var, Node node)
        { throw new UnsupportedOperationException("BindingProject.add1") ; }

        protected void checkAdd1(Var var, Node node)
        {}

        protected boolean contains1(Var var)
        {
            // In the projection set and the underlying
            // binding (OPTIONAL means it may not be in this binding) 
            return projectionVars.contains(var) && binding.contains(var) ;
            //return projectionVars.contains(var) ; 
        }

        protected Node get1(Var var)
        {
            if ( ! projectionVars.contains(var) )
                return null ; 
            return binding.get(var) ;
        }

        protected Iterator vars1()
        {
            return projectionVars.iterator() ;
        }

        protected int size1()
        {
            return projectionVars.size() ;
        }
    }
}

/*
 * (c) Copyright 2005, 2006, 2007 Hewlett-Packard Development Company, LP
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