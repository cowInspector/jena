package tx;

public class DevTx
{
    // Transaction needs to do FileRef to file. 
    
    // Delete BlockMgrFile.
    
    // Building a dataset.
    //   Commit and reply?
    //   Reattach a journal?
    
    // Need a clearly defined start/finish 
    //   start => location, journal and set datastructures.
    
    // TestObjectFileTrans -- more tests.
    // TestObjectFileBuffering --> make abstract, it stress tests the BufferChannel.
    
    // One transaction dataset - reuse.  Pool?
    //   Create the one transaction dataset when the datasetgraph itself is created
    //   Reset/Reuse -- internal API : TransactionCycle. 
    // Record BlockMgrs, BlockMgrTx.reset ==> .begin
    
    // Channel+Adler32
    
    // Journal
    // Either entry based, write/read like 
    //   Or buffer slicing version.
    //   Allocate space to include the reader.  Seems liek more work for less reason.
    
    // Tidy up 
    //   See HACK (BPTreeNode)
    //   See [TxTDB:PATCH-UP]
    //   See [TxTDB:TODO]
    //   See FREE
    //   See [ITER]
    
    // Caching pages across actions sequences. e.g BPT root block.
    // Iterators
    //   Cache in transaction so forgettable?
    //   Iterator tracking replaces epoch mechanism?
    //   (No - it's a step of iterator that complains, not the update.) 
    
    // ---- ---- ---- ----

    // Optimizations:
    //   ByteBuffer.allocateDirect + pooling
    //     http://mail-archives.apache.org/mod_mbox/mina-dev/200804.mbox/%3C47F90DF0.6050101@gmail.com%3E
    //     http://mail-archives.apache.org/mod_mbox/mina-dev/200804.mbox/%3Cloom.20080407T064019-708@post.gmane.org%3E
    //   ObjectFileTrans - avoid copy!

    // Other:
    //   Sort out IndexBulder/IndexFactory/(IndexMaker in test)
    
    /*
     * Iterator tracking
     *   End transaction => close all open iterators.
     *   Need transaction - at least something to attach for tracking.
     *     ==> Add "transaction txn" to all RangeIndex operations.  Default null -> no transaction.
     *     OR
     *     ==> Add to B+Tree   .setTransaction(Transaction txn) 
     *   End transaction -> close block managers -> checking? 
     *   
     * Recycle DatasetGraphTx objects.  Setup - set PageView
     *   better setup.
     */

    /*
     * Layers:
     *   DatasetGraph
     *   Indexes
     *   Pages
     *   Blocks
     *   Storage = BlockAccess (a sequence of blocks) and BufferChannel  
     */
    
    /* 
     * Fast B+Tree creation: wrap an existsing BPTree with another that switches the block managers only.
     *    BPTree.attach with warpping BlockMgrs.
     *    Delay creation of some things?
     * Cache root block.
     * Setup
     *   Transaction start: grab alloc id.
     */
    
    // TDB 0.8.10 is rev 8718; TxTDB forked at 8731
    // Diff of SF ref 8718 to Apache cross over applied. (src/ only)
    // Now Apache: rev 1124661 
}
