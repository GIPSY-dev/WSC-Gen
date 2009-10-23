/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.objectives.MinimizeSqrErrorObjective.java
 * Last modification: 2007-09-22
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

package test.org.sigoa.cnMotor;

import java.io.Serializable;

import org.sigoa.refimpl.go.objectives.ObjectiveFunction;
import org.sigoa.refimpl.go.objectives.ObjectiveState;

/**
 * This objective function puts pressure into the direction of programs
 * with smaller square errors.
 * 
 * @author Thomas Weise
 */
public class MinimizeSqrErrorObjective
    extends
    ObjectiveFunction<double[], ObjectiveState, Serializable, MotorAccessor> {

  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create the objective function
   */
  public MinimizeSqrErrorObjective() {
    super(null);
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
    sb.append("sqr_error"); //$NON-NLS-1$
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
    return MotorAccessor.class;
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
   * @param staticData
   *          not used, <code>null</code>
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
  public void endEvaluation(final double[] individual,
      final ObjectiveState state, final Serializable staticData,
      final MotorAccessor simulation) {
    state.setObjectiveValue(simulation.getSquareError());
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
   * @return The count of simulation steps needed. Values 0 indidicate that
   *         no simulation needs to be performed for this objective
   *         function.
   * @throws NullPointerException
   *           if <code>individual==null</code> or
   *           <code>state==null</code>
   */
  @Override
  public long getRequiredSimulationSteps(final double[] individual,
      final ObjectiveState state, final Serializable staticState) {
    return 1;
  }

  /**
   * <p>
   * This method is called before any simulation or optimization will be
   * performed. It is used to check whether an individual is worth being
   * processed further. Normally, this method will return <code>true</code>
   * or performs only a very fast and simple check.
   * </p>
   * <p>
   * This method is only called once per individual.
   * </p>
   * <p>
   * If this method returns
   * <code>false/<code>, all objective values of the individual will be set
   * to positive infinity and also no other objective function is invoked
   * on the individual
   * </p>
   *
   * @param individual
   *          The individual to be checked.
   * @param staticState
   *          the static state record
   * @return <code>true</code> if and only if the individual is worth
   *         being examined. <code>false</code> if it can be thrown away.
   *
   * @throws NullPointerException if <code>individual==null</code>
   */
  @Override
  public boolean sanityCheck(final double[] individual,
      final Serializable staticState) {
    return CNMotorTestSeries.EDITOR.isValid(individual);
  }
}
