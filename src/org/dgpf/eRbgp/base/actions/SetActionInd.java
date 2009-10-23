/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.eRbgp.base.actions.SetAction.java
 * Last modification: 2008-04-18
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

package org.dgpf.eRbgp.base.actions;

import org.dgpf.eRbgp.base.Construct;
import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.RBGPProgramBase;
import org.dgpf.rbgp.base.Symbol;
import org.dgpf.rbgp.base.SymbolSet;
import org.dgpf.vm.base.VirtualMachine;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * the set action
 * 
 * @author Thomas Weise
 */
public class SetActionInd extends
    Action<VirtualMachine<RBGPMemory, RBGPProgramBase>> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the index
   */
  Symbol m_symbol;

  /**
   * the cache
   */
  static Symbol[] CACHE;

  /**
   * Create a new node
   * 
   * @param symbol
   *          the symbol
   * @param children
   *          the child nodes
   */
  public SetActionInd(final Node[] children, final Symbol symbol) {
    super(children);
    this.m_symbol = symbol;
  }

  /**
   * Initialize the cache
   * 
   * @param symbols
   *          the symbol set
   */
  public static final void initSymbols(final SymbolSet symbols) {
    int i;

    i = symbols.size();
    CACHE = new Symbol[i];

    for (--i; i >= 0; i--) {
      CACHE[i] = symbols.get(i);
    }
  }

  /**
   * Execute this construct
   * 
   * @param vm
   *          the virtual machine
   * @return an arbitrary return value
   */
  @Override
  @SuppressWarnings("unchecked")
  public int execute(final VirtualMachine<RBGPMemory, RBGPProgramBase> vm) {
    int v;
    final int[] m;
    final int s;

    s = vm.m_memory.m_mem1[this.m_symbol.m_id];
    if ((0 > s) || (s >= CACHE.length) || (CACHE[s].m_writeProtected)) {
      vm.setError();
      return 0;
    }

    v = ((Construct) (this.get(0))).execute(vm);
    m = vm.m_memory.m_mem2;
    if (v != m[s]) {
      m[s] = v;
      return -1;
    }
    return 0;
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    sb.append('[');
    this.m_symbol.toStringBuilder(sb);
    sb.append("] = "); //$NON-NLS-1$
    this.get(0).toStringBuilder(sb);
  }

  /**
   * Obtain the factory which deals with nodes of the same type as this
   * node.
   * 
   * @return the factory which deals with nodes of the same type as this
   *         node
   */
  @Override
  public INodeFactory getFactory() {
    return SetFactory.SET_IND_FACTORY;
  }

  /**
   * Serializes the parameters of the constructor of this object.
   * 
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  @Override
  protected void javaParametersToStringBuilder(final StringBuilder sb,
      final int indent) {
    super.javaParametersToStringBuilder(sb, indent);
    sb.append(", "); //$NON-NLS-1$
    this.m_symbol.javaToStringBuilder(sb, indent);
  }

  /**
   * check for equality
   * 
   * @param o
   *          the object to compare with
   * @return <code>true</code> if and only if the two objects equal
   */
  @Override
  public boolean equals(final Object o) {
    if (!(super.equals(o)))
      return false;
    return ((SetActionInd) o).m_symbol.equals(this.m_symbol);
  }
}
