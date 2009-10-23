/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-01-09
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.benchmark.geccoTests.SuccessParser.java
 * Last modification: 2008-01-09
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

package test.org.dgpf.benchmark.geccoTests;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.io.CanonicalFile;
import org.sfc.io.IO;
import org.sfc.io.TextWriter;
import org.sfc.io.csv.CSVReader;
import org.sfc.math.Mathematics;

/**
 * This program helps to build csvs.
 * 
 * @author Thomas Weise
 */
public class RuggedSuccessParser {

  /**
   * the parse dir
   */
  private static String PARSE_DIR = "D:\\Programming\\Java\\dgpfTests\\benchmark\\ruggedness"; //$NON-NLS-1$

  /**
   * the output dir
   */
  private static String OUTPUT_DIR = "E:\\ruggedness"; //$NON-NLS-1$

  /**
   * the param count
   */
  private static final int PARAM_CNT = 1;

  /**
   * the num idx
   */
  private static final int CNT_IDX = PARAM_CNT;

  /**
   * the first s
   */
  private static final int MIN_S_IDX = CNT_IDX + 1;

  /**
   * the avg s
   */
  private static final int AVG_S_IDX = MIN_S_IDX + 1;

  /**
   * the max s
   */
  private static final int MAX_S_IDX = AVG_S_IDX + 1;

  /**
   * the number of failed
   */
  private static final int FAILED_S_CNT_IDX = MAX_S_IDX + 1;

  /**
   * the number of failed
   */
  private static final int S_VAR_IDX = FAILED_S_CNT_IDX + 1;

  /**
   * the first s
   */
  private static final int MIN_P_IDX = S_VAR_IDX + 1;

  /**
   * the avg s
   */
  private static final int AVG_P_IDX = MIN_P_IDX + 1;

  /**
   * the max s
   */
  private static final int MAX_P_IDX = AVG_P_IDX + 1;

  /**
   * the number of failed
   */
  private static final int FAILED_P_CNT_IDX = MAX_P_IDX + 1;

  /**
   * the number of failed
   */
  private static final int P_VAR_IDX = FAILED_P_CNT_IDX + 1;

  /**
   * the len
   */
  private static final int LEN = P_VAR_IDX + 1;

  /** q */
  private static final int Q = 25;

  /**
   * parse the series dir.
   * 
   * @param dir
   *          the directory
   * @return the data [param,
   */
  private static final double[] parseSeries(final File dir) {
    File[] fs;
    long[] gen;
    double[] d;
    String[] x;
    int i, j;
    File f;
    String v;
    long q;

    if (dir == null)
      return null;
    fs = dir.listFiles();
    if (fs == null)
      return null;

    if ((i = fs.length) > 0) {
      gen = new long[LEN];
      gen[MIN_S_IDX] = Long.MAX_VALUE;
      gen[MIN_P_IDX] = Long.MAX_VALUE;
      gen[MAX_S_IDX] = Long.MIN_VALUE;
      gen[MAX_P_IDX] = Long.MIN_VALUE;

      for (--i; i >= 0; i--) {
        f = fs[i];
        if (f.isDirectory()) {
          if (new File(f, "resultObjectives.txt").exists()) { //$NON-NLS-1$

            f = new File(f, "observeSuccess.txt");//$NON-NLS-1$
            if (f.exists()) {
              parse(f, gen);
            }
          }
        }
      }

      d = new double[gen.length];
      for (i = 0; i < PARAM_CNT; i++)
        d[i] = gen[i];

      d[CNT_IDX] = gen[CNT_IDX];
      d[MIN_S_IDX] = gen[MIN_S_IDX];
      q = (gen[CNT_IDX] - gen[FAILED_S_CNT_IDX]);
      d[AVG_S_IDX] = (q > 0) ? // 
      (gen[AVG_S_IDX] / (double) (q))
          : Double.NaN;
      d[MAX_S_IDX] = gen[MAX_S_IDX];
      d[FAILED_S_CNT_IDX] = gen[FAILED_S_CNT_IDX];
      d[S_VAR_IDX] = (q > 0) ? ((gen[S_VAR_IDX] / ((double) q)) - (d[AVG_S_IDX] * d[AVG_S_IDX]))
          : Double.NaN;

      d[MIN_P_IDX] = gen[MIN_P_IDX];
      q = (gen[CNT_IDX] - gen[FAILED_P_CNT_IDX]);
      d[AVG_P_IDX] = (q > 0) ? // 
      (gen[AVG_P_IDX] / (double) (q))
          : Double.NaN;
      d[MAX_P_IDX] = gen[MAX_P_IDX];
      d[FAILED_P_CNT_IDX] = gen[FAILED_P_CNT_IDX];
      d[P_VAR_IDX] = (q > 0) ? ((gen[P_VAR_IDX] / ((double) q)) - (d[AVG_P_IDX] * d[AVG_P_IDX]))
          : Double.NaN;

      x = dir.getName().split(","); //$NON-NLS-1$
      for (i = 0; i < Math.min(x.length, PARAM_CNT); i++) {
        v = x[i];
        j = v.indexOf('=');
        if (j > 0)
          v = v.substring(j + 1);
        try {
          d[i] = comp((int) (0.5d + Double.parseDouble(v)));
        } catch (Throwable t) {
          //
        }
      }

      return d;

    }

    return null;
  }

  /**
   * parse the files
   * 
   * @param file
   *          the file
   * @param l
   *          the longs
   */
  private static final void parse(final Object file, final long[] l) {
    String[][] s;
    int i;
    long v;

    l[CNT_IDX]++;

    s = CSVReader.readCSVFile(file, false);

    if (s != null) {
      i = 1;
      for (i = 1; i < s.length; i++) {
        if ((s[i] != null) && (s[i].length >= 2) && //
            (s[i][1].equalsIgnoreCase("s") || //$NON-NLS-1$
            s[i][1].equalsIgnoreCase("p"))) { //$NON-NLS-1$
          break;
        }
      }

      v = -1;
      if (i < s.length)
        try {
          v = Long.parseLong(s[i][0]);
        } catch (Throwable t) {
          v = -1;
        }

      if (v < 0) {
        l[FAILED_S_CNT_IDX]++;
        l[FAILED_P_CNT_IDX]++;
        return;
      }

      l[MAX_S_IDX] = Math.max(l[MAX_S_IDX], v);
      l[MIN_S_IDX] = Math.min(l[MIN_S_IDX], v);
      l[AVG_S_IDX] += v;
      l[S_VAR_IDX] += (v * v);

      for (; i < s.length; i++) {
        if ((s[i] != null) && (s[i].length >= 2) && //
            (s[i][1].equalsIgnoreCase("p"))) { //$NON-NLS-1$
          break;
        }
      }

      v = -1;
      if (i < s.length)
        try {
          v = Long.parseLong(s[i][0]);
        } catch (Throwable t) {
          v = -1;
        }

      if (v < 0) {
        l[FAILED_P_CNT_IDX]++;
        return;
      }

      l[MAX_P_IDX] = Math.max(l[MAX_P_IDX], v);
      l[MIN_P_IDX] = Math.min(l[MIN_P_IDX], v);
      l[AVG_P_IDX] += v;
      l[P_VAR_IDX] += (v * v);
    }

  }

  /**
   * Parse the directory
   * 
   * @param dir
   *          the dir
   * @return the data
   */
  private static final List<double[]> parseDirs(final File dir) {
    File[] fs;
    List<double[]> l;
    double[] d;
    int i;

    fs = dir.listFiles();
    if ((i = fs.length) > 0) {
      l = CollectionUtils.createList(i);

      for (--i; i >= 0; i--) {
        d = parseSeries(fs[i]);
        if (d != null)
          l.add(d);
      }

      Collections.sort(l, new Comparator<double[]>() {
        public int compare(double[] d1, double d2[]) {
          int ii, j;
          for (ii = 0; ii < PARAM_CNT; ii++) {
            j = Double.compare(d1[ii], d2[ii]);
            if (j != 0)
              return j;
          }
          return 0;
        }
      });

      return l;
    }

    return null;
  }

  /**
   * compute
   * 
   * @param gamma
   * @return x
   */
  private static final int comp(final int gamma) {
    int i, j, k;

    if (gamma <= 0)
      return 0;

    i = (int) ((Q + .5d) - Math.sqrt(Q * Q - Q + 2.25 - (2 * gamma)));
    j = (gamma - (Q * (i - 1) - ((i * (i - 1)) / 2)) - 1);
    k = ((Q * (Q - 1)) >>> 1);

    if ((i & 1) == 1) {
      return (gamma - ((Q - 1) * (i >>> 1)) + ((i >>> 1) * (i >>> 1)));
    }

    return k - (Q * ((i >>> 1) - 1)) + (((i >>> 1) - 1) * ((i >>> 1) - 1))
        + ((i >>> 1) - 1) - j;
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(final String[] args) {
    CanonicalFile pd, od;
    List<double[]> l;
    double[] d;
    int v, i, x;
    final TextWriter minS, avgS, maxS, minP, avgP, maxP, failS, failP, sSP, fSP, sVar, sStdDev, pSP, fpP, pVar, pStdDev;
    boolean bminS, bavgS, bmaxS, bminP, bavgP, bmaxP, bfailS, bfailP, bsSP, bfSP, bsVar, bsStdDev, bpSP, bfpP, bpVar, bpStdDev;

    System.out.println(comp(57));
    System.exit(0);
    
    pd = IO.getFile(PARSE_DIR);
    od = IO.getFile(OUTPUT_DIR);

    od.mkdirs();

    l = parseDirs(pd);

    if ((v = l.size()) > 0) {

      minS = new TextWriter(new File(od, "minS.txt")); //$NON-NLS-1$
      maxS = new TextWriter(new File(od, "maxS.txt")); //$NON-NLS-1$
      avgS = new TextWriter(new File(od, "avgS.txt")); //$NON-NLS-1$
      failS = new TextWriter(new File(od, "failS.txt")); //$NON-NLS-1$

      sSP = new TextWriter(new File(od, "successSFrac.txt")); //$NON-NLS-1$
      fSP = new TextWriter(new File(od, "failedSFrac.txt")); //$NON-NLS-1$
      sVar = new TextWriter(new File(od, "varS.txt")); //$NON-NLS-1$
      sStdDev = new TextWriter(new File(od, "stddevS.txt")); //$NON-NLS-1$

      minP = new TextWriter(new File(od, "minP.txt")); //$NON-NLS-1$
      maxP = new TextWriter(new File(od, "maxP.txt")); //$NON-NLS-1$
      avgP = new TextWriter(new File(od, "avgP.txt")); //$NON-NLS-1$
      failP = new TextWriter(new File(od, "failP.txt")); //$NON-NLS-1$

      pSP = new TextWriter(new File(od, "successPFrac.txt")); //$NON-NLS-1$
      fpP = new TextWriter(new File(od, "failedPFrac.txt")); //$NON-NLS-1$
      pVar = new TextWriter(new File(od, "varP.txt")); //$NON-NLS-1$
      pStdDev = new TextWriter(new File(od, "stddevP.txt")); //$NON-NLS-1$

      for (i = 0; i < v; i++) {
        d = l.get(i);

        bminS = (Mathematics.isNumber(d[MIN_S_IDX]));

        bavgS = (Mathematics.isNumber(d[AVG_S_IDX]));

        bmaxS = (Mathematics.isNumber(d[MAX_S_IDX]));

        bfailS = (Mathematics.isNumber(d[FAILED_S_CNT_IDX]));

        bsSP = (Mathematics.isNumber((d[CNT_IDX] - d[FAILED_S_CNT_IDX])
            / d[CNT_IDX]));

        bfSP = (Mathematics.isNumber((d[FAILED_S_CNT_IDX]) / d[CNT_IDX]));

        bsVar = (Mathematics.isNumber(d[S_VAR_IDX]));

        bsStdDev = (Mathematics.isNumber(d[S_VAR_IDX]));

        bminP = (Mathematics.isNumber(d[MIN_P_IDX]));

        bavgP = (Mathematics.isNumber(d[AVG_P_IDX]));

        bmaxP = (Mathematics.isNumber(d[MAX_P_IDX]));

        bfailP = (Mathematics.isNumber(d[FAILED_P_CNT_IDX]));

        bpSP = (Mathematics.isNumber((d[CNT_IDX] - d[FAILED_P_CNT_IDX])
            / d[CNT_IDX]));

        bfpP = (Mathematics.isNumber((d[FAILED_P_CNT_IDX]) / d[CNT_IDX]));

        bpVar = (Mathematics.isNumber(d[P_VAR_IDX]));

        bpStdDev = (Mathematics.isNumber(d[P_VAR_IDX]));

        if (bminS)
          minS.ensureNewLine();
        if (bmaxS)
          maxS.ensureNewLine();
        if (bavgS)
          avgS.ensureNewLine();
        if (bfailS)
          failS.ensureNewLine();
        if (bminP)
          minP.ensureNewLine();
        if (bmaxP)
          maxP.ensureNewLine();
        if (bavgP)
          avgP.ensureNewLine();
        if (bfailP)
          failP.ensureNewLine();
        if (bsSP)
          sSP.ensureNewLine();
        if (bsVar)
          sVar.ensureNewLine();
        if (bfSP)
          fSP.ensureNewLine();
        if (bsStdDev)
          sStdDev.ensureNewLine();
        if (bpSP)
          pSP.ensureNewLine();
        if (bpVar)
          pVar.ensureNewLine();
        if (bfpP)
          fpP.ensureNewLine();
        if (bpStdDev)
          pStdDev.ensureNewLine();

        int y;

        for (x = 0; x < PARAM_CNT; x++) {
          y = (int) (0.5d + d[x]);
          if (bminS) {
            minS.writeDouble(y);
            minS.writeCSVSeparator();
          }
          if (bmaxS) {
            maxS.writeDouble(y);
            maxS.writeCSVSeparator();
          }
          if (bavgS) {
            avgS.writeDouble(y);
            avgS.writeCSVSeparator();
          }
          if (bfailS) {
            failS.writeDouble(y);
            failS.writeCSVSeparator();
          }
          if (bminP) {
            minP.writeDouble(y);
            minP.writeCSVSeparator();
          }
          if (bmaxP) {
            maxP.writeDouble(y);
            maxP.writeCSVSeparator();
          }
          if (bavgP) {
            avgP.writeDouble(y);
            avgP.writeCSVSeparator();
          }
          if (bfailP) {
            failP.writeDouble(y);
            failP.writeCSVSeparator();
          }

          if (bsSP) {
            sSP.writeDouble(y);
            sSP.writeCSVSeparator();
          }
          if (bsVar) {
            sVar.writeDouble(y);
            sVar.writeCSVSeparator();
          }
          if (bfSP) {
            fSP.writeDouble(y);
            fSP.writeCSVSeparator();
          }
          if (bsStdDev) {
            sStdDev.writeDouble(y);
            sStdDev.writeCSVSeparator();
          }

          if (bpSP) {
            pSP.writeDouble(y);
            pSP.writeCSVSeparator();
          }
          if (bpVar) {
            pVar.writeDouble(y);
            pVar.writeCSVSeparator();
          }
          if (bfpP) {
            fpP.writeDouble(y);
            fpP.writeCSVSeparator();
          }
          if (bpStdDev) {
            pStdDev.writeDouble(y);
            pStdDev.writeCSVSeparator();
          }
        }

        if (Mathematics.isNumber(d[MIN_S_IDX]))
          minS.writeDouble(d[MIN_S_IDX]);

        if (Mathematics.isNumber(d[AVG_S_IDX]))
          avgS.writeDouble(d[AVG_S_IDX]);

        if (Mathematics.isNumber(d[MAX_S_IDX]))
          maxS.writeDouble(d[MAX_S_IDX]);

        if (Mathematics.isNumber(d[FAILED_S_CNT_IDX]))
          failS.writeDouble(d[FAILED_S_CNT_IDX]);

        if (Mathematics.isNumber((d[CNT_IDX] - d[FAILED_S_CNT_IDX])
            / d[CNT_IDX]))
          sSP.writeDouble((d[CNT_IDX] - d[FAILED_S_CNT_IDX]) / d[CNT_IDX]);

        if (Mathematics.isNumber((d[FAILED_S_CNT_IDX]) / d[CNT_IDX]))
          fSP.writeDouble((d[FAILED_S_CNT_IDX]) / d[CNT_IDX]);

        if (Mathematics.isNumber(d[S_VAR_IDX]))
          sVar.writeDouble(d[S_VAR_IDX]);

        if (Mathematics.isNumber(d[S_VAR_IDX]))
          sStdDev.writeDouble(Math.sqrt(d[S_VAR_IDX]));

        if (Mathematics.isNumber(d[MIN_P_IDX]))
          minP.writeDouble(d[MIN_P_IDX]);

        if (Mathematics.isNumber(d[AVG_P_IDX]))
          avgP.writeDouble(d[AVG_P_IDX]);

        if (Mathematics.isNumber(d[MAX_P_IDX]))
          maxP.writeDouble(d[MAX_P_IDX]);

        if (Mathematics.isNumber(d[FAILED_P_CNT_IDX]))
          failP.writeDouble(d[FAILED_P_CNT_IDX]);

        if (Mathematics.isNumber((d[CNT_IDX] - d[FAILED_P_CNT_IDX])
            / d[CNT_IDX]))
          pSP.writeDouble((d[CNT_IDX] - d[FAILED_P_CNT_IDX]) / d[CNT_IDX]);

        if (Mathematics.isNumber((d[FAILED_P_CNT_IDX]) / d[CNT_IDX]))
          fpP.writeDouble((d[FAILED_P_CNT_IDX]) / d[CNT_IDX]);

        if (Mathematics.isNumber(d[P_VAR_IDX]))
          pVar.writeDouble(d[P_VAR_IDX]);

        if (Mathematics.isNumber(d[P_VAR_IDX]))
          pStdDev.writeDouble(Math.sqrt(d[P_VAR_IDX]));
      }

      failP.release();
      avgP.release();
      maxP.release();
      minP.release();

      failS.release();
      avgS.release();
      maxS.release();
      minS.release();

      sSP.release();
      fSP.release();
      sVar.release();
      sStdDev.release();
      pSP.release();
      fpP.release();
      pVar.release();
      pStdDev.release();
    }
  }
}
