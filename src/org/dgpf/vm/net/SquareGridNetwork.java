/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-08
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.net.SquareGridNetwork.java
 * Last modification: 2007-10-08
 *                by: Thomas Weise
 * 
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to theFree Software
 *                    Foundation, Inc. 51 Franklin Street, Fifth Floor,
 *                    Boston, MA 02110-1301, USA or download the license
 *                    under http://www.gnu.org/licenses/lgpl.html or
 *                    http://www.gnu.org/copyleft/lesser.html.
 *                    
 * Warranty         : This software is provided "as is" without any
 *                    warranty; without even the implied warranty of
 *                    merchantability or fitness for a particular purpose.
 *                    See the Gnu Lesser General Public License for more
 *                    details.
 */

package org.dgpf.vm.net;

import java.io.Serializable;

import org.dgpf.vm.base.VirtualMachineProgram;

/**
 * This type of network uses a fixed grid as topology.
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the virtual machine program type
 * @param <MDT>
 *          the message data type
 * @author Thomas Weise
 */
public class SquareGridNetwork<MT extends Serializable, PT extends VirtualMachineProgram<MT>, MDT extends Serializable>
    extends Network<MT, PT, MDT> {

  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new network.
   * 
   * @param provider
   *          the network provider
   */
  @SuppressWarnings("unchecked")
  public SquareGridNetwork(final NetworkProvider<MT> provider) {
    super(provider);

    NetVirtualMachine<MT, PT, MDT>[] vms;
    int i;

    vms = this.m_vms;
    for (i = (vms.length - 1); i >= 0; i--) {
      vms[i].m_neighbors = new NetVirtualMachine[4];
    }
  }

  /**
   * Initialize the network
   */
  @Override
  protected void initNetwork() {
    NetVirtualMachine<MT, PT, MDT>[] vms;
    int i, w, j, x, y, t, u;
    NetVirtualMachine<MT, PT, MDT> v;

    super.initNetwork();

    vms = this.m_vms;
    i = this.m_vmCount;// this.m_provider.m_vmCount[this.m_simIndex];
    w = Math.max(1, (int) (Math.sqrt(i) + 0.5d));

    for (j = (i - 1); j >= 0; j--) {
      y = (j / w);
      x = (j % w);

      v = vms[j];
      u = 0;

      t = (((y + 1) * w) + x);
      if (t < i)
        v.m_neighbors[u++] = vms[t];

      if (y > 0)
        v.m_neighbors[u++] = vms[(((y - 1) * w) + x)];

      if ((j < (i - 1)) && (x < (w-1)))
        v.m_neighbors[u++] = vms[j + 1];

      if (x > 0)
        v.m_neighbors[u++] = vms[j - 1];

      v.m_neighborCount = u;
    }
    
    return;
  }

  // /**
  // * Deliver a certain message.
  // *
  // * @param message
  // * the message to be delivered
  // */
  // @Override
  // @SuppressWarnings("unchecked")
  // protected final void send(final Message<MDT> message) {
  // int i;
  // Message<MDT> m;
  // final NetVirtualMachine<MT, PT, MDT>[] vms;
  // final NetVirtualMachine<?, ?, MDT> v1;
  // NetVirtualMachine<MT, PT, MDT> v2;
  //
  // v1 = message.m_owner;
  // vms = ((NetVirtualMachine[]) (v1.m_neighbors));
  // m = null;
  //
  // for (i = (v1.m_neighborCount - 1); i >= 0; i--) {
  // v2 = vms[i];
  // if (v2.m_alive) {
  // if (i > 0) {
  // if ((m = copyMessage(message)) == null)
  // return;
  // } else
  // m = message;
  // this.deliver(m, v2);
  // }
  // }
  //
  // if (m != message)
  // NetVirtualMachine.disposeMessage(message);
  // }
}
