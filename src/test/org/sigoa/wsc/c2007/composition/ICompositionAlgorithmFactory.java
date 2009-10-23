/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-06-11
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.composition.ICompositionAlgorithmFactory.java
 * Last modification: 2007-06-11
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

package test.org.sigoa.wsc.c2007.composition;

/**
 * The factory for composition algorithms
 *
 * @author Thomas Weise
 */
public interface ICompositionAlgorithmFactory {

  /**
   * Create a composition algorithm
   *
   * @return the new composition algorithm
   */
  public abstract ICompositionAlgorithm createCompositionAlgorithm();

}
