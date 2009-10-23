/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.simulation.VarianceObjectiveFunction.java
 * Last modification: 2007-03-27
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

package org.dgpf.aggregation.alternative;

import java.io.Serializable;

import org.dgpf.aggregation.net.AggregationNetProgram;
import org.sigoa.refimpl.go.objectives.ObjectiveFunction;
import org.sigoa.refimpl.go.objectives.ObjectiveState;
import org.sigoa.spec.go.objectives.IObjectiveValueComputer;

/**
 * This fitness function puts pressure into the direction of programs that
 * produce lower variance.
 * 
 * @author Thomas Weise
 */
public class VarianceObjectiveFunction
    extends
    ObjectiveFunction<AggregationNetProgram, ObjectiveState, Serializable, Calculation> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the required step count
   */
  private final long m_stepCnt;

  /**
   * Create a new aggregation fitness function.
   * 
   * @param ovc
   *          The objective value computer to be used. If this is
   *          <code>null</code>, the <code>ObjectiveUtils.AVG_OVC</code>
   *          is used as default.
   * @param params
   *          the parameters
   */
  public VarianceObjectiveFunction(final CalculationParameters params,
      final IObjectiveValueComputer ovc) {
    super(ovc);
    this.m_stepCnt = (params.getStepsPerTest());
  }

  /**
   * Create a new aggregation fitness function.
   * 
   * @param params
   *          the parameters
   */
  public VarianceObjectiveFunction(final CalculationParameters params) {
    this(params, null);
  }

  /**
   * Obtain the count of simulation steps that will be needed in order to
   * evaluate the specified individual.
   * 
   * @param individual
   *          The individual to be evaluated.
   * @param state
   *          The state container.
   * @param staticState
   *          the static state record
   * @return The count of simulation steps needed. This default
   *         implementation returns 0.
   * @throws NullPointerException
   *           if <code>individual==null</code> or
   *           <code>state==null</code>
   */
  @Override
  public long getRequiredSimulationSteps(
      final AggregationNetProgram individual, final ObjectiveState state,
      final Serializable staticState) {
    if ((individual == null) || (state == null))
      throw new NullPointerException();
    return this.m_stepCnt;
  }

  /**
   * This method is called after any simulation/evaluation is performed.
   * After this method returns, an objective value must have been stored in
   * the state record.
   * 
   * @param individual
   *          The individual that should be evaluated next.
   * @param state
   *          The state record.
   * @param staticState
   *          the static state record
   * @param simulation
   *          The simulation (<code>null</code> if no simulation is
   *          required as indivicated by
   *          <code>getRequiredSimulationSteps</code>).
   * @throws NullPointerException
   *           if <code>individual==null</code> or
   *           <code>state==null</code> or if
   *           <code>simulator==null</code> but a simulation is required.
   */
  @Override
  public void endEvaluation(final AggregationNetProgram individual,
      final ObjectiveState state, final Serializable staticState,
      final Calculation simulation) {
    double d;
    d = simulation.m_var;
    if (d > Double.MIN_VALUE) {
      state.setObjectiveValue(Calculation.applyWeight(d, individual
          .getWeight()));
    }
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
    sb.append("variance"); //$NON-NLS-1$
  }

  /**
   * Obtain the id of the required simulator. If <code>null</code> is
   * returned, no simulation will be needed/performed for objective
   * function.
   * 
   * @return The id of the simulator required for the evaluation of this
   *         objective function.
   */
  @Override
  public Serializable getRequiredSimulationId() {
    return Calculation.class;
  }
}
