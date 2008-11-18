/*
 * (c) Copyright 2007, 2008 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package dev;

public class Dev
{
    // Tuple reader and checking IRIs (with caching)
    
    // Scripted test slow?
    // IGraphTDB renaming.
    // GraphTDB > GraphTriplesTDB, GraphQuadsTDB : GraphFactory
    // Or 
    //      GraphTDB > GraphNamedTDB (with ".tuple(Triple)")
    //   Optimization of quad patterns - optimize associated BGP
    //   Quad loader.
    //   Test cases, include substitution.
    // OpRange

    // ---- Reopenable BlockMgrs (and the object file?) 

    // Cheap parsing of Node table.
    
    // --  ?? ARQ Optimizer and controlling optimizations per graph (esp FilterPlacement).
    
    // Umbel data: http://umbel.org/documentation.html
    
    // ---- Assembler and configuration
    //    Configuring the built-in constants.
    //    Setting options (globally, per model)
    //    Variable indexes
    
    // ---- Materialized views
    
    // ---- Range queries (filter E (bgp T)) ==> (range T E....) where E has one var and the one var is in T
    //    Given NodeId structure, should be doable for ints and dates.
    
    // ---- Reifier
    
    // ---- Quads
    //       dataset management
    
    // ---- Loader - traverse of SPO via a direct block mgr?
    //   For quads
    //   ** Close indexes not in use in a given phase
    //  GraphTDB.primaryTraverse() -> Iterator<Tuple<NodeId>>??
    //    Do properly : reopenable indexes (under the triple index)
    //      Reopenable block mgr's .passivate/.activate
    //    Open second index on same
    //    - shared formatting with GraphLoadMonitor
    // ---- tdbloader: 
    //   ** (script) to work on gzip files
    //   ** Write stats (where to hook in to get unique?)
    // ---- 32 bit mode.
    // ARC Cache Scheme

    //   TransformCopy 
    //      - maybe Transform should be "Op transform(OpABC)"
    //      - and TransformCopy(list) is TransformRewrite. (there are no non-TransformCopy's)
    
    // ------------------------------------------
    
    // ---- New cache API alloc/return/invalidate (shrink/grow?) : stats
    // Weak references and more space.
    // ARC policy
    
    // ---- Node Table
    //     Compression?
    
    // ---- Graph
    // QueryHandler to access subjectsFor etc. 
    // removeAll implementation: depends on iterator.remove
    // but can do faster as a specific operation.

    // Version of NodeTable that does Logical => Physical id translation
    //    And a PageMgr wrapper for same?

    // Inlines => Inline56, Inline64

    // Update BDB form
}
