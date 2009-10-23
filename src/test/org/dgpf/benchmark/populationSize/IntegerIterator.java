/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.benchmark.populationSize.IntegerIterator.java
 * Last modification: 2007-10-22
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

package test.org.dgpf.benchmark.populationSize;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.sfc.text.ITextable;
import org.sigoa.refimpl.stoch.Randomizer;

/**
 * the integer iterator which can be used for example for iterating
 * population sizes
 * 
 * @author Thomas Weise
 */
public class IntegerIterator extends Number implements Iterator<Object[]>,
    ITextable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the minimum population size
   */
  private final int m_minPs;

  /**
   * the maximum population size
   */
  private final int m_maxPs;

  /**
   * the step size
   */
  private final int m_stepSize;

  /**
   * the current population size
   */
  private int m_current;

  /**
   * the data
   */
  private final Object[] m_data;

  /**
   * Create a new integer iterator
   * 
   * @param minPs
   *          the minimum population size
   * @param maxPs
   *          the maximum population size
   * @param stepSize
   *          the step size
   */
  public IntegerIterator(final int minPs, final int maxPs,
      final int stepSize) {
    super();
    this.m_minPs = minPs;
    this.m_maxPs = maxPs;
    this.m_stepSize = stepSize;
    this.m_current = minPs
        + (stepSize * new Randomizer()
            .nextInt(((maxPs - minPs) / stepSize) + 1));

    this.m_data = new Object[] { this };
  }

  /**
   * Returns the value of the specified number as an <code>int</code>.
   * This may involve rounding or truncation.
   * 
   * @return the numeric value represented by this object after conversion
   *         to type <code>int</code>.
   */
  @Override
  public int intValue() {
    return this.m_current;
  }

  /**
   * Returns the value of the specified number as a <code>long</code>.
   * This may involve rounding or truncation.
   * 
   * @return the numeric value represented by this object after conversion
   *         to type <code>long</code>.
   */
  @Override
  public long longValue() {
    return this.m_current;
  }

  /**
   * Returns the value of the specified number as a <code>float</code>.
   * This may involve rounding.
   * 
   * @return the numeric value represented by this object after conversion
   *         to type <code>float</code>.
   */
  @Override
  public float floatValue() {
    return this.m_current;
  }

  /**
   * Returns the value of the specified number as a <code>double</code>.
   * This may involve rounding.
   * 
   * @return the numeric value represented by this object after conversion
   *         to type <code>double</code>.
   */
  @Override
  public double doubleValue() {
    return this.m_current;
  }

  /**
   * Returns <tt>true</tt> if the iteration has more elements. (In other
   * words, returns <tt>true</tt> if <tt>next</tt> would return an
   * element rather than throwing an exception.)
   * 
   * @return <tt>true</tt> if the iterator has more elements.
   */
  public boolean hasNext() {
    return true;
  }

  /**
   * Returns the next element in the iteration. Calling this method
   * repeatedly until the {@link #hasNext()} method returns false will
   * return each element in the underlying collection exactly once.
   * 
   * @return the next element in the iteration.
   * @exception NoSuchElementException
   *              iteration has no more elements.
   */
  public Object[] next() {
    this.m_current += this.m_stepSize;
    if (this.m_current > this.m_maxPs)
      this.m_current = this.m_minPs;
    return this.m_data;
  }

  /**
   * Removes from the underlying collection the last element returned by
   * the iterator (optional operation). This method can be called only once
   * per call to <tt>next</tt>. The behavior of an iterator is
   * unspecified if the underlying collection is modified while the
   * iteration is in progress in any way other than by calling this method.
   * 
   * @exception UnsupportedOperationException
   *              if the <tt>remove</tt> operation is not supported by
   *              this Iterator.
   * @exception IllegalStateException
   *              if the <tt>next</tt> method has not yet been called, or
   *              the <tt>remove</tt> method has already been called
   *              after the last call to the <tt>next</tt> method.
   */
  public void remove() {
    //
  }

  /**
   * Obtain the string representation of this iterator
   * 
   * @return the string representation of this iterator
   */
  @Override
  public String toString() {
    return String.valueOf(this.m_current);
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  public void toStringBuilder(final StringBuilder sb) {
    sb.append(this.m_current);
  }
}
