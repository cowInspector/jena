/*
 * (c) Copyright 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * [See end of file]
 */

/* Generated By:JJTree: Do not edit this line. Q_Query.java */

package com.hp.hpl.jena.sparql.lang.rdql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.graph.Triple;

import com.hp.hpl.jena.sparql.ARQInternalErrorException;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.syntax.ElementFilter;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.util.ALog;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;

/** Concrete result of parsing a query.
 *  This is the top node in the abstract syntax tree generated by the jjtree/javacc grammar.
 *  After being created this class builds a Query suitable for execution.  After that,
 *  this is not used, although many of the syntax tree nodes are used as they implement
 *  the interfaces needed by the abstarct query model.
 */

public class Q_Query extends SimpleNode
{
    
    public Q_Query(int id) { super(id); }

    public Q_Query(RDQLParser p, int id) { super(p, id); }

    private Query query = null ;

    // --------------------------------------------------------------------

    boolean constructStar = false ;
    boolean selectAllVars = false ;

    // Post parsing fixups.
    // One - collect prefixes.
    // Two build in memory structures.
    
    // This is to rearrange the parse tree (pull up the structural nodes
    // and remove the need for later casting).
    // It separates the parse tree from the query processor.
    // By the end, the only classes of relevance should be:
    //      Var
    //      Expr
    //      TriplePattern
    // all nicely inserted into the Query object.

    // This code could live in the parser object itself but then it is stored in the
    // .jjt file, making development harder.

    public void rdqlPhase2(Query q)
    {
        query = q ;
        ElementGroup queryElement = new ElementGroup() ; 
        query.setQueryPattern(queryElement) ;
        
        // Collect all mentioned variables.
        List varAcc = new ArrayList() ;

        try {
            // First - collect prefixes.
            this.postParse1(q) ;
            
            // Second create nodes
            this.postParse2(q) ;
            
            int numQueryChildren = jjtGetNumChildren() ;
            
            int i = 0 ;
            // Select
            if ( jjtGetChild(i) instanceof Q_SelectClause ) 
            {
                q.setQueryType(Query.QueryTypeSelect) ;
                extractVarList(q, jjtGetChild(i)) ;
                i++ ;
            }
            
            if ( q.isUnknownType() )
                throw new ARQInternalErrorException("Parser didn't catch absence of SELECT clause") ;

            // Source
            if ( i < numQueryChildren )
            {
                if ( jjtGetChild(i) instanceof Q_SourceClause )
                {
                    // SourceClause -> SourceSelector -> URL
                    int numSources = jjtGetChild(i).jjtGetNumChildren() ;
                    if ( numSources > 1 )
                    {
                        throw new QueryException("Error: Multiple sources in FROM clause") ;  
                    }
                    
                    // This code is waiting to be fixed for multiple sources
                    // That requires an interface change to Query   
                    for ( int j = 0 ; j < numSources ; j++ )
                    {                
                        RDQL_Node n = jjtGetChild(i).jjtGetChild(j).jjtGetChild(0) ;
                        String source = ((Q_URL)n).urlString ;
                        q.addGraphURI(source) ;
                    }
                    i++ ;
                }
            }
            // Triple patterns

            // WHERE
            if ( i < numQueryChildren )
            {
                if ( jjtGetChild(i) instanceof Q_TriplePatternClause )
                {
                    // Convert to graph-level query.
                    makeElementTriplePatterns(q, jjtGetChild(i), queryElement, varAcc) ;
                    i++ ;
                }
            }
            
            // Constraints

            if ( i < numQueryChildren )
            {
	            if ( jjtGetChild(i) instanceof Q_ConstraintClause )
	            {
                    makeElementConstraints(q, queryElement, jjtGetChild(i)) ;
                    //qm.setConstraints(elt) ;
	                i++ ;
	            }
            }
            
            if ( constructStar )
            {
                //for ( Iterator iter = qm.getTriplePatterns().iterator() ; iter.hasNext() ; )
                //    q.addRulePattern((Triple)iter.next()) ;
            }
            
            if ( selectAllVars )
            {
                query.setQueryResultStar(true) ;
                for ( Iterator iter = varAcc.iterator() ; iter.hasNext() ; )
                {
                    String varName = (String)iter.next() ;
                    q.addResultVar(varName) ;
                }
            }
        }
        catch (ARQInternalErrorException e) { throw e ; }
        catch (QueryException qEx) { throw qEx; } 
        catch (ClassCastException e) { throw new ARQInternalErrorException("Parser generated illegal parse tree", e) ; }
        catch (Exception e)
        {
            ALog.warn(this, "Unknown exception",e) ;
            throw new ARQInternalErrorException("Unknown exception: "+e) ;
        }
    }


    /** Formats the query from phase 2 in a style that is acceptable to the
     *  parser.  Note this is NOT guaranteed to be the same as the original string
     *  because we may have done optimizations or other rearranging.
     *  It should give the same answers on the same dataset.
     */

    @Override
    public String toString()
    {
    	throw new UnsupportedOperationException ("Q_Query.toString()") ;
    }

    private void extractVarList(Query q, RDQL_Node node)
    {
        int n = node.jjtGetNumChildren() ;
        selectAllVars = ( n == 0 ) ;

        for ( int i = 0 ; i < n ; i++ )
        {
            RDQL_Node c = node.jjtGetChild(i) ;
            if ( ! (c instanceof Q_Var) )
                throw new ARQInternalErrorException("Internal error: parser created '"+c.getClass().getName()+"' when Q_Var expected") ;
            Q_Var v = (Q_Var)c ;
            q.addResultVar(v.varName) ;
        }
    }

    private void extractVarOrURIList(Query q, RDQL_Node node)
    {
        int n = node.jjtGetNumChildren() ;
        selectAllVars = ( n == 0 ) ;

        for ( int i = 0 ; i < n ; i++ )
        {
            RDQL_Node c = node.jjtGetChild(i) ;
            if ( c instanceof Q_Var )
            {
                Q_Var v = (Q_Var)c ;
                q.addResultVar(v.varName) ;
                continue ;
            }
            if ( ( c instanceof Q_URI ) || ( c instanceof Q_QName ) )
            {
                Q_URI v = (Q_URI)c ;
                
                q.addDescribeNode(v.getNode()) ;
                continue ;
            }
            throw new ARQInternalErrorException("Internal error: parser created '"+c.getClass().getName()+"' when Q_Var expected") ;
        }
    }

    private void makeElementTriplePatterns(Query q, RDQL_Node node, ElementGroup group, List varAcc)
    {
        int n = node.jjtGetNumChildren() ;
        
        for ( int j = 0 ; j < n ; j++ )
        {
            Q_TriplePattern tp = (Q_TriplePattern)node.jjtGetChild(j) ;
            if ( tp.jjtGetNumChildren() != 3 )
                throw new ARQInternalErrorException("Triple pattern has "+tp.jjtGetNumChildren()+" children") ;

            com.hp.hpl.jena.graph.Node nodeSubj = convertToGraphNode(tp.jjtGetChild(0), q, varAcc) ;
            com.hp.hpl.jena.graph.Node nodePred = convertToGraphNode(tp.jjtGetChild(1), q, varAcc) ;
            com.hp.hpl.jena.graph.Node nodeObj  = convertToGraphNode(tp.jjtGetChild(2), q, varAcc) ;
            Triple t = new Triple(nodeSubj, nodePred, nodeObj) ;
            group.addTriplePattern(t) ;
        }
    }


    private void extractFixedTriplePatterns(Query q, List triplePatterns, RDQL_Node node, List varAcc)
    {
        int n = node.jjtGetNumChildren() ;
        for ( int j = 0 ; j < n ; j++ )
        {
            Q_TriplePattern tp = (Q_TriplePattern)node.jjtGetChild(j) ;
            if ( tp.jjtGetNumChildren() != 3 )
                throw new ARQInternalErrorException("Triple pattern has "+tp.jjtGetNumChildren()+" children") ;

            com.hp.hpl.jena.graph.Node nodeSubj = convertToGraphNode(tp.jjtGetChild(0), q, varAcc) ;
            com.hp.hpl.jena.graph.Node nodePred = convertToGraphNode(tp.jjtGetChild(1), q, varAcc) ;
            com.hp.hpl.jena.graph.Node nodeObj  = convertToGraphNode(tp.jjtGetChild(2), q, varAcc) ;
            triplePatterns.add(new Triple(nodeSubj, nodePred, nodeObj)) ;
        }
    }


    // This operation puts all the things to graph node conversion code in
    // one place.
    
    
    static private com.hp.hpl.jena.graph.Node convertToGraphNode(RDQL_Node n, Query q, List varAcc)
    {
        if ( n instanceof Q_Var )
        {
            String varName = ((Q_Var)n).getName() ;
            if ( varAcc != null )
                varAcc.add(varName) ;
            return Var.alloc(((Q_Var)n).getName()) ;
        }
        if ( n instanceof ParsedLiteral)
        {
            ParsedLiteral v = (ParsedLiteral)n ;
            
            if ( v.isNode() )
                return v.getNode() ;

            if ( v.isURI() )
                return com.hp.hpl.jena.graph.Node.createURI(v.getURI()) ;
                
            if ( v.isString() )
                return com.hp.hpl.jena.graph.Node.createLiteral(v.getString(), null, null) ;

            if ( v.isBoolean())
                return com.hp.hpl.jena.graph.Node.createLiteral(v.asUnquotedString(), null,null) ;
            
            if ( v.isInt() )
                return com.hp.hpl.jena.graph.Node.createLiteral(
                        v.asUnquotedString(), null, XSDDatatype.XSDinteger) ;
            if ( v.isDouble())
                return com.hp.hpl.jena.graph.Node.createLiteral(
                    v.asUnquotedString(), null, XSDDatatype.XSDdouble) ;

            String s = v.getString() ;
            ALog.fatal(Q_Query.class, "Failed to convert: "+s) ;
        }
        throw new ARQInternalErrorException("convertToGraphNode encountered strange type: "+n.getClass().getName()) ;
                                                                
    }

    private void makeElementConstraints(Query q, ElementGroup g ,RDQL_Node node)
    {
        Q_ConstraintClause qcc = (Q_ConstraintClause)node ;
        int n = qcc.jjtGetNumChildren() ;
        for ( int j = 0 ; j < n ; j++ )
        {
            Object obj = qcc.jjtGetChild(j) ;
            if ( ! ( obj instanceof ExprRDQL ) )
                throw new ARQInternalErrorException("Parse node in AND clause isn't a Constraint") ;
            ElementFilter f = new ElementFilter((ExprRDQL)obj) ;
            g.addElementFilter(f) ;
        }
    }
    
    private void extractPrefixes(Query q, Q_PrefixesClause qns)
    {
    	if ( qns == null )
    		return ;
    
        int n = qns.jjtGetNumChildren() ;
        for ( int j = 0 ; j < n ; j++ )
        {
            Q_PrefixDecl qnsd = (Q_PrefixDecl)qns.jjtGetChild(j) ;
            // They should appear in pairs: an identifier and a URI
            for ( int k = 0 ; k < qnsd.jjtGetNumChildren() ; k+=2 )
            {
                Q_Identifier id = (Q_Identifier)qnsd.jjtGetChild(k) ;
                //Object tmp = qnsd.jjtGetChild(k+1) ; // Temp debug
                Q_URI uri = (Q_URI)qnsd.jjtGetChild(k+1) ;
                query.setPrefix(id.toString(), uri.toString()) ;
            }
        }
    }
}

/*
 *  (c) Copyright 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 *  All rights reserved.
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
