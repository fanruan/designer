package com.fr.design.layout;


import java.util.NoSuchElementException;
import java.util.StringTokenizer;


/**
 * TableLayoutConstraints binds components to their constraints.
 */
public class TableLayoutConstraints {
    /** Cell in which the upper left corner of the component lays */
    public int col1;
    /** Cell in which the upper left corner of the component lays */
    public int row1;

    /** Cell in which the lower right corner of the component lays */
    public int col2;
    /** Cell in which the lower right corner of the component lays */
    public int row2;

    /** Horizontal justification if component occupies just one cell */
    public int hAlign;

    /** Verical justification if component occupies just one cell */
    public int vAlign;

    public TableLayoutConstraints() {
        col1 = row1 = col1 = col2 = 0;
        hAlign = vAlign = TableLayout.FULL;
    }

    public TableLayoutConstraints(int col, int row) {
        this(col, row, col, row, TableLayout.FULL, TableLayout.FULL);
    }

    /**
     * Constructs an TableLayoutConstraints a set of constraints.
     *
     * @param col1      column where upper-left cornor of the component is placed
     * @param row1      row where upper-left cornor of the component is placed
     * @param col2      column where lower-right cornor of the component is placed
     * @param row2      row where lower-right cornor of the component is placed
     */
    public TableLayoutConstraints(int col1, int row1, int col2, int row2) {
        this(col1, row1, col2, row2, TableLayout.FULL, TableLayout.FULL);
    }

    /**
     * Constructs an TableLayoutConstraints a set of constraints.
     *
     * @param col1      column where upper-left cornor of the component is placed
     * @param row1      row where upper-left cornor of the component is placed
     * @param col2      column where lower-right cornor of the component is placed
     * @param row2      row where lower-right cornor of the component is placed
     * @param hAlign    horizontal justification of a component in a single cell
     * @param vAlign    vertical justification of a component in a single cell
     */

    public TableLayoutConstraints
            (int col1, int row1, int col2, int row2, int hAlign, int vAlign) {
        this.col1 = col1;
        this.row1 = row1;
        this.col2 = col2;
        this.row2 = row2;

        if ((hAlign == TableLayout.LEFT) ||
                (hAlign == TableLayout.RIGHT) ||
                (hAlign == TableLayout.CENTER) ||
                (hAlign == TableLayout.FULL) ||
                (hAlign == TableLayout.LEADING) ||
                (hAlign == TableLayout.TRAILING)) {
            this.hAlign = hAlign;
        } else
            this.hAlign = TableLayout.FULL;

        if ((vAlign == TableLayout.LEFT) ||
                (vAlign == TableLayout.RIGHT) ||
                (vAlign == TableLayout.CENTER)) {
            this.vAlign = vAlign;
        } else
            this.vAlign = TableLayout.FULL;
    }


    /**
     * Constructs an TableLayoutConstraints from a string.
     *
     * @param constraints    indicates TableLayoutConstraints's position and
     *                       justification as a string in the form "column, row" or
     *                       "column, row, horizontal justification, vertical
     *                       justification" or "column 1, row 1, column 2, row 2"
     *                       or "column 1, row 1, column 2, row 2, horizontal
     *                       justification, vertical justification".
     *                       It is also acceptable to delimit the paramters with
     *                       spaces instead of commas.
     */

    public TableLayoutConstraints(String constraints) {
        // Use default values for any parameter not specified or specified
        // incorrectly.  The default parameters place the component in a single
        // cell at column 0, row 0.  The component is fully justified.
        col1 = 0;
        row1 = 0;
        col2 = 0;
        row2 = 0;
        hAlign = TableLayout.FULL;
        vAlign = TableLayout.FULL;

        // Parse constraints using spaces or commas
        StringTokenizer st = new StringTokenizer(constraints, ", ");
        int numToken = st.countTokens();

        try {
            // Check constraints
            if ((numToken != 2) && (numToken != 4) && (numToken != 6))
                throw new RuntimeException();

            // Get the first column (assume component is in only one column)
            String tokenA = st.nextToken();
            col1 = Integer.parseInt(tokenA);
            col2 = col1;

            // Get the first row (assume component is in only one row)
            String tokenB = st.nextToken();
            row1 = Integer.parseInt(tokenB);
            row2 = row1;

            // Get next two tokens
            tokenA = st.nextToken();
            tokenB = st.nextToken();

            try {
                // Attempt to use tokens A and B as col2 and row2
                col2 = Integer.parseInt(tokenA);
                row2 = Integer.parseInt(tokenB);

                // Get next two tokens
                tokenA = st.nextToken();
                tokenB = st.nextToken();
            } catch (NumberFormatException error) {
                col2 = col1;
                row2 = row1;
            }

            // Check if token means horizontally justification the component
            if ((tokenA.equalsIgnoreCase("L")) || (tokenA.equalsIgnoreCase("LEFT")))
                hAlign = TableLayout.LEFT;
            else if ((tokenA.equalsIgnoreCase("C")) ||
                    (tokenA.equalsIgnoreCase("CENTER")))
                hAlign = TableLayout.CENTER;
            else if ((tokenA.equalsIgnoreCase("F")) ||
                    (tokenA.equalsIgnoreCase("FULL")))
                hAlign = TableLayout.FULL;
            else if ((tokenA.equalsIgnoreCase("R")) ||
                    (tokenA.equalsIgnoreCase("RIGHT")))
                hAlign = TableLayout.RIGHT;
            else if ((tokenA.equalsIgnoreCase("LD")) ||
                    (tokenA.equalsIgnoreCase("LEADING")))
                hAlign = TableLayout.LEADING;
            else if ((tokenA.equalsIgnoreCase("TL")) ||
                    (tokenA.equalsIgnoreCase("TRAILING")))
                hAlign = TableLayout.TRAILING;
            else
                throw new RuntimeException();

            // Check if token means horizontally justification the component
            if ((tokenB.equalsIgnoreCase("T")) || (tokenB.equalsIgnoreCase("TOP")))
                vAlign = TableLayout.TOP;
            else if ((tokenB.equalsIgnoreCase("C")) ||
                    (tokenB.equalsIgnoreCase("CENTER")))
                vAlign = TableLayout.CENTER;
            else if ((tokenB.equalsIgnoreCase("F")) ||
                    (tokenB.equalsIgnoreCase("FULL")))
                vAlign = TableLayout.FULL;
            else if ((tokenB.equalsIgnoreCase("B")) ||
                    (tokenB.equalsIgnoreCase("BOTTOM")))
                vAlign = TableLayout.BOTTOM;
            else
                throw new RuntimeException();
        } catch (NoSuchElementException error) {
        } catch (RuntimeException error) {
            throw new IllegalArgumentException
                    ("Expected constraints in one of the following formats:\n" +
                    "  col1, row1\n  col1, row1, col2, row2\n" +
                    "  col1, row1, hAlign, vAlign\n" +
                    "  col1, row1, col2, row2, hAlign, vAlign\n" +
                    "Constraints provided '" + constraints + "'");
        }

        // Make sure row2 >= row1
        if (row2 < row1)
            row2 = row1;

        // Make sure col2 >= col1
        if (col2 < col1)
            col2 = col1;
    }


    /**
     * Gets a string representation of this TableLayoutConstraints.
     *
     * @return a string in the form "row 1, column 1, row 2, column 2, horizontal
     *         justification, vertical justification"
     */

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(col1);
        buffer.append(", ");
        buffer.append(row1);
        buffer.append(", ");

        buffer.append(col2);
        buffer.append(", ");
        buffer.append(row2);
        buffer.append(", ");

        final String h[] = {"left", "center", "full", "right", "leading",
                            "trailing"};
        final String v[] = {"top", "center", "full", "bottom"};

        buffer.append(h[hAlign]);
        buffer.append(", ");
        buffer.append(v[vAlign]);

        return buffer.toString();
    }
}