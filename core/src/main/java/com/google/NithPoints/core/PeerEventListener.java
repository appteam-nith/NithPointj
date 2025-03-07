/**
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.NithPoints.core;

import java.util.List;

/**
 * <p>Implementors can listen to events like blocks being downloaded/transactions being broadcast/connect/disconnects,
 * they can pre-filter messages before they are procesesed by a {@link Peer} or {@link PeerGroup}, and they can
 * provide transactions to remote peers when they ask for them.</p>
 */
public interface PeerEventListener {
    /**
     * Called on a Peer thread when a block is received.<p>
     *
     * The block may have transactions or may be a header only once getheaders is implemented.
     *
     * @param peer       the peer receiving the block
     * @param block      the downloaded block
     * @param blocksLeft the number of blocks left to download
     */
    void onBlocksDownloaded(Peer peer, Block block, int blocksLeft);

    /**
     * Called when a download is started with the initial number of blocks to be downloaded.
     *
     * @param peer       the peer receiving the block
     * @param blocksLeft the number of blocks left to download
     */
    void onChainDownloadStarted(Peer peer, int blocksLeft);

    /**
     * Called when a peer is connected
     *
     * @param peer
     * @param peerCount the total number of connected peers
     */
    void onPeerConnected(Peer peer, int peerCount);

    /**
     * Called when a peer is disconnected. Note that this won't be called if the listener is registered on a
     * {@link PeerGroup} and the group is in the process of shutting down.
     *
     * @param peer
     * @param peerCount the total number of connected peers
     */
    void onPeerDisconnected(Peer peer, int peerCount);

    /**
     * Called when a message is received by a peer, before the message is processed. The returned message is
     * processed instead. Returning null will cause the message to be ignored by the Peer returning the same message
     * object allows you to see the messages received but not change them. The result from one event listeners
     * callback is passed as "m" to the next, forming a chain.
     */
    Message onPreMessageReceived(Peer peer, Message m);

    /**
     * Called when a new transaction is broadcast over the network.
     */
    void onTransaction(Peer peer, Transaction t);

    /**
     * Called when a peer receives a getdata message, usually in response to an "inv" being broadcast. Return as many
     * items as possible which appear in the {@link GetDataMessage}, or null if you're not interested in responding.
     */
    List<Message> getData(Peer peer, GetDataMessage m);

    /**
     * Called if there is an exception thrown in a Netty worker thread whilst processing an inbound message. You
     * can use this to report crashes of the peer threads back to your apps website, for instance. After this callback
     * runs the peer will be disconnected. Any exceptions thrown by this method will be logged and ignored.
     */
    void onException(Throwable throwable);
}
