
package com.fr.design.gui.syntax.ui.rsyntaxtextarea.modes;

import java.io.*;
import javax.swing.text.Segment;

import com.fr.design.gui.syntax.ui.rsyntaxtextarea.*;


%%

%public
%class FormulaTokenMaker
%extends AbstractJFlexCTokenMaker
%unicode
%type com.fr.design.gui.syntax.ui.rsyntaxtextarea.Token


%{

	/**
	 * Constructor.  This must be here because JFlex does not generate a
	 * no-parameter constructor.
	 */
	public FormulaTokenMaker() {
		super();
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 * @see #addToken(int, int, int)
	 */
	private void addHyperlinkToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so, true);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
	private void addToken(int tokenType) {
		addToken(zzStartRead, zzMarkedPos-1, tokenType);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
	private void addToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param array The character array.
	 * @param start The starting offset in the array.
	 * @param end The ending offset in the array.
	 * @param tokenType The token's type.
	 * @param startOffset The offset in the document at which this token
	 *                    occurs.
	 */
	@Override
	public void addToken(char[] array, int start, int end, int tokenType, int startOffset) {
		super.addToken(array, start,end, tokenType, startOffset);
		zzStartRead = zzMarkedPos;
	}


	/**
	 * Returns the text to place at the beginning and end of a
	 * line to "comment" it in a this programming language.
	 *
	 * @return The start and end strings to add to a line to "comment"
	 *         it out.
	 */
	@Override
	public String[] getLineCommentStartAndEnd() {
		return new String[] { "//", null };
	}


	/**
	 * Returns the first token in the linked list of tokens generated
	 * from <code>text</code>.  This method must be implemented by
	 * subclasses so they can correctly implement syntax highlighting.
	 *
	 * @param text The text from which to get tokens.
	 * @param initialTokenType The token type we should start with.
	 * @param startOffset The offset into the document at which
	 *                    <code>text</code> starts.
	 * @return The first <code>Token</code> in a linked list representing
	 *         the syntax highlighted text.
	 */
	public Token getTokenList(Segment text, int initialTokenType, int startOffset) {

		resetTokenList();
		this.offsetShift = -text.offset + startOffset;

		// Start off in the proper state.
		int state = Token.NULL;
		switch (initialTokenType) {
			case Token.COMMENT_MULTILINE:
				state = MLC;
				start = text.offset;
				break;
			default:
				state = Token.NULL;
		}

		s = text;
		try {
			yyreset(zzReader);
			yybegin(state);
			return yylex();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return new TokenImpl();
		}

	}


	/**
	 * Refills the input buffer.
	 *
	 * @return      <code>true</code> if EOF was reached, otherwise
	 *              <code>false</code>.
	 * @exception   IOException  if any I/O-Error occurs.
	 */
	private boolean zzRefill() throws java.io.IOException {
		return zzCurrentPos>=s.offset+s.count;
	}


	/**
	 * Resets the scanner to read from a new input stream.
	 * Does not close the old reader.
	 *
	 * All internal variables are reset, the old input stream
	 * <b>cannot</b> be reused (internal buffer is discarded and lost).
	 * Lexical state is set to <tt>YY_INITIAL</tt>.
	 *
	 * @param reader   the new input stream
	 */
	public final void yyreset(java.io.Reader reader) throws java.io.IOException {
		// 's' has been updated.
		zzBuffer = s.array;
		/*
		 * We replaced the line below with the two below it because zzRefill
		 * no longer "refills" the buffer (since the way we do it, it's always
		 * "full" the first time through, since it points to the segment's
		 * array).  So, we assign zzEndRead here.
		 */
		//zzStartRead = zzEndRead = s.offset;
		zzStartRead = s.offset;
		zzEndRead = zzStartRead + s.count - 1;
		zzCurrentPos = zzMarkedPos = zzPushbackPos = s.offset;
		zzLexicalState = YYINITIAL;
		zzReader = reader;
		zzAtBOL  = true;
		zzAtEOF  = false;
	}

	public final String test() {
	    return "abs|ABS|COUNT|count";
	}


%}

Letter				= [A-Za-z]
LetterOrUnderscore	= ({Letter}|[_])
Digit				= [0-9]
HexDigit			= {Digit}|[A-Fa-f]
OctalDigit			= [0-7]
Exponent			= [eE][+-]?{Digit}+

PreprocessorWord	= define|elif|else|endif|error|if|ifdef|ifndef|include|line|pragma|undef

Trigraph				= ("??="|"??("|"??)"|"??/"|"??'"|"??<"|"??>"|"??!"|"??-")

OctEscape1			= ([\\]{OctalDigit})
OctEscape2			= ([\\]{OctalDigit}{OctalDigit})
OctEscape3			= ([\\][0-3]{OctalDigit}{OctalDigit})
OctEscape				= ({OctEscape1}|{OctEscape2}|{OctEscape3})
HexEscape				= ([\\][xX]{HexDigit}{HexDigit})

AnyChrChr					= ([^\'\n\\])
Escape					= ([\\]([abfnrtv\'\"\?\\0]))
UnclosedCharLiteral			= ([\']({Escape}|{OctEscape}|{HexEscape}|{Trigraph}|{AnyChrChr}))
CharLiteral				= ({UnclosedCharLiteral}[\'])
ErrorUnclosedCharLiteral		= ([\'][^\'\n]*)
ErrorCharLiteral			= (([\'][\'])|{ErrorUnclosedCharLiteral}[\'])
AnyStrChr					= ([^\"\n\\])
FalseTrigraph				= (("?"(("?")*)[^\=\(\)\/\'\<\>\!\-\\\?\"\n])|("?"[\=\(\)\/\'\<\>\!\-]))
StringLiteral				= ([\"]((((("?")*)({Escape}|{OctEscape}|{HexEscape}|{Trigraph}))|{FalseTrigraph}|{AnyStrChr})*)(("?")*)[\"])
UnclosedStringLiteral		= ([\"]([\\].|[^\\\"])*[^\"]?)
ErrorStringLiteral			= ({UnclosedStringLiteral}[\"])


LineTerminator		= \n
WhiteSpace		= [ \t\f]

MLCBegin			= "/*"
MLCEnd			= "*/"
LineCommentBegin	= "//"

NonFloatSuffix		= (([uU][lL]?)|([lL][uU]?))
IntegerLiteral		= ({Digit}+{Exponent}?{NonFloatSuffix}?)
HexLiteral		= ("0"[xX]{HexDigit}+{NonFloatSuffix}?)
FloatLiteral		= ((({Digit}*[\.]{Digit}+)|({Digit}+[\.]{Digit}*)){Exponent}?[fFlL]?)
ErrorNumberFormat	= (({IntegerLiteral}|{HexLiteral}|{FloatLiteral}){NonSeparator}+)

NonSeparator		= ([^\t\f\r\n\ \(\)\{\}\[\]\;\,\.\=\>\<\!\~\?\:\+\-\*\/\&\|\^\%\"\']|"#")
Identifier		= ({LetterOrUnderscore}({LetterOrUnderscore}|{Digit}|[$])*)
ErrorIdentifier	= ({NonSeparator}+)


URLGenDelim				= ([:\/\?#\[\]@])
URLSubDelim				= ([\!\$&'\(\)\*\+,;=])
URLUnreserved			= ({LetterOrUnderscore}|{Digit}|[\-\.\~])
URLCharacter			= ({URLGenDelim}|{URLSubDelim}|{URLUnreserved}|[%])
URLCharacters			= ({URLCharacter}*)
URLEndCharacter			= ([\/\$]|{Letter}|{Digit})
URL						= (((https?|f(tp|ile))"://"|"www.")({URLCharacters}{URLEndCharacter})?)

FunctionNames = "ABS"|
                "abs"|
                "ACOS"|
                "acos"|
                "ACOSH"|
                "acosh"|
                "ADD2ARRAY"|
                "add2array"|
                "AND"|
                "and"|
                "ARRAY"|
                "array"|
                "ASIN"|
                "asin"|
                "ASINH"|
                "asinh"|
                "ATAN"|
                "atan"|
                "ATAN2"|
                "atan2"|
                "ATANH"|
                "atanh"|
                "AVERAGE"|
                "average"|
                "BITNOT"|
                "bitnot"|
                "BITOPERATION"|
                "bitoperation"|
                "CEILING"|
                "ceiling"|
                "CHAR"|
                "char"|
                "CIRCULAR"|
                "circular"|
                "CLASS"|
                "class"|
                "CNMONEY"|
                "cnmoney"|
                "CODE"|
                "code"|
                "COL"|
                "col"|
                "COLCOUNT"|
                "colcount"|
                "COLNAME"|
                "colname"|
                "COMBIN"|
                "combin"|
                "CONCATENATE"|
                "concatenate"|
                "CORREL"|
                "correl"|
                "COS"|
                "cos"|
                "COSH"|
                "cosh"|
                "COUNT"|
                "count"|
                "CROSSLAYERTOTAL"|
                "crosslayertotal"|
                "DATE"|
                "date"|
                "DATEDELTA"|
                "datedelta"|
                "DATEDIF"|
                "datedif"|
                "DATEINMONTH"|
                "dateinmonth"|
                "DATEINQUARTER"|
                "dateinquarter"|
                "DATEINWEEK"|
                "dateinweek"|
                "DATEINYEAR"|
                "dateinyear"|
                "DATESUBDATE"|
                "datesubdate"|
                "DATETONUMBER"|
                "datetonumber"|
                "DAY"|
                "day"|
                "DAYS360"|
                "days360"|
                "DAYSOFMONTH"|
                "daysofmonth"|
                "DAYSOFQUARTER"|
                "daysofquarter"|
                "DAYSOFYEAR"|
                "daysofyear"|
                "DAYVALUE"|
                "dayvalue"|
                "DECIMAL"|
                "decimal"|
                "DECODE"|
                "decode"|
                "DEGREES"|
                "degrees"|
                "ENCODE"|
                "encode"|
                "ENDWITH"|
                "endwith"|
                "ENMONEY"|
                "enmoney"|
                "ENNUMBER"|
                "ennumber"|
                "EVAL"|
                "eval"|
                "EVEN"|
                "even"|
                "EXACT"|
                "exact"|
                "EXP"|
                "exp"|
                "FACT"|
                "fact"|
                "FIELDS"|
                "fields"|
                "FILENAME"|
                "filename"|
                "FILESIZE"|
                "filesize"|
                "FILETYPE"|
                "filetype"|
                "FIND"|
                "find"|
                "FLOOR"|
                "floor"|
                "FORMAT"|
                "format"|
                "GETUSERDEPARTMENTS"|
                "getuserdepartments"|
                "GETUSERJOBTITLES"|
                "getuserjobtitles"|
                "GREPARRAY"|
                "greparray"|
                "HIERARCHY"|
                "hierarchy"|
                "HOUR"|
                "hour"|
                "I18N"|
                "i18n"|
                "IF"|
                "if"|
                "INARRAY"|
                "inarray"|
                "INDEX"|
                "index"|
                "INDEXOF"|
                "indexof"|
                "INDEXOFARRAY"|
                "indexofarray"|
                "INT"|
                "int"|
                "ISNULL"|
                "isnull"|
                "JOINARRAY"|
                "joinarray"|
                "LAYERTOTAL"|
                "layertotal"|
                "LEFT"|
                "left"|
                "LEN"|
                "len"|
                "LET"|
                "let"|
                "LN"|
                "ln"|
                "LOG"|
                "log"|
                "LOG10"|
                "log10"|
                "LOWER"|
                "lower"|
                "LUNAR"|
                "lunar"|
                "MAP"|
                "map"|
                "MAPARRAY"|
                "maparray"|
                "MAX"|
                "max"|
                "MEDIAN"|
                "median"|
                "MID"|
                "mid"|
                "MIN"|
                "min"|
                "MINUTE"|
                "minute"|
                "MOD"|
                "mod"|
                "MOM"|
                "mom"|
                "MONTH"|
                "month"|
                "MONTHDELTA"|
                "monthdelta"|
                "NOW"|
                "now"|
                "NUMTO"|
                "numto"|
                "NVL"|
                "nvl"|
                "ODD"|
                "odd"|
                "OR"|
                "or"|
                "PI"|
                "pi"|
                "POWER"|
                "power"|
                "PRODUCT"|
                "product"|
                "PROMOTION"|
                "promotion"|
                "PROPER"|
                "proper"|
                "PROPORTION"|
                "proportion"|
                "RADIANS"|
                "radians"|
                "RAND"|
                "rand"|
                "RANDBETWEEN"|
                "randbetween"|
                "RANGE"|
                "range"|
                "RANK"|
                "rank"|
                "RECORDS"|
                "records"|
                "REGEXP"|
                "regexp"|
                "REMOVEARRAY"|
                "removearray"|
                "REPEAT"|
                "repeat"|
                "REPLACE"|
                "replace"|
                "REVERSE"|
                "reverse"|
                "REVERSEARRAY"|
                "reversearray"|
                "RIGHT"|
                "right"|
                "ROUND"|
                "round"|
                "ROUND5"|
                "round5"|
                "ROUNDDOWN"|
                "rounddown"|
                "ROUNDUP"|
                "roundup"|
                "ROW"|
                "row"|
                "ROWCOUNT"|
                "rowcount"|
                "SECOND"|
                "second"|
                "SEQ"|
                "seq"|
                "SIGN"|
                "sign"|
                "SIN"|
                "sin"|
                "SINH"|
                "sinh"|
                "SLICEARRAY"|
                "slicearray"|
                "SORT"|
                "sort"|
                "SORTARRAY"|
                "sortarray"|
                "SPLIT"|
                "split"|
                "SQL"|
                "sql"|
                "SQRT"|
                "sqrt"|
                "STARTWITH"|
                "startwith"|
                "STDEV"|
                "stdev"|
                "SUBSTITUTE"|
                "substitute"|
                "SUM"|
                "sum"|
                "SUMSQ"|
                "sumsq"|
                "SWITCH"|
                "switch"|
                "TABLEDATAFIELDS"|
                "tabledatafields"|
                "TABLEDATAS"|
                "tabledatas"|
                "TABLES"|
                "tables"|
                "TAN"|
                "tan"|
                "TANH"|
                "tanh"|
                "TIME"|
                "time"|
                "TOBINARY"|
                "tobinary"|
                "TODATE"|
                "todate"|
                "TODAY"|
                "today"|
                "TODOUBLE"|
                "todouble"|
                "TOHEX"|
                "tohex"|
                "TOIMAGE"|
                "toimage"|
                "TOINTEGER"|
                "tointeger"|
                "TOOCTAL"|
                "tooctal"|
                "TREELAYER"|
                "treelayer"|
                "TRIM"|
                "trim"|
                "TRUNC"|
                "trunc"|
                "UNIQUEARRAY"|
                "uniquearray"|
                "UPPER"|
                "upper"|
                "UUID"|
                "uuid"|
                "VALUE"|
                "value"|
                "WEEK"|
                "week"|
                "WEEKDATE"|
                "weekdate"|
                "WEEKDAY"|
                "weekday"|
                "WEIGHTEDAVERAGE"|
                "weightedaverage"|
                "YEAR"|
                "year"|
                "YEARDELTA"|
                "yeardelta"|
                "SUMPRECISE"|
                "sumprecise"|
                "QUERY"|
                "query"|
                "WEBIMAGE"|
                "webimage"


%state MLC
%state EOL_COMMENT

%%

<YYINITIAL> {

	/* Standard functions */
	{FunctionNames}				{ addToken(Token.RESERVED_WORD); }


	{LineTerminator}				{ addNullToken(); return firstToken; }

	{Identifier}					{ addToken(Token.IDENTIFIER); }

	{WhiteSpace}+					{ addToken(Token.WHITESPACE); }

	/* Preprocessor directives */
	"#"{WhiteSpace}*{PreprocessorWord}	{ addToken(Token.PREPROCESSOR); }

	/* String/Character Literals. */
	{CharLiteral}					{ addToken(Token.LITERAL_CHAR); }
	{UnclosedCharLiteral}			{ addToken(Token.ERROR_CHAR); /*addNullToken(); return firstToken;*/ }
	{ErrorUnclosedCharLiteral}		{ addToken(Token.ERROR_CHAR); addNullToken(); return firstToken; }
	{ErrorCharLiteral}				{ addToken(Token.ERROR_CHAR); }
	{StringLiteral}				{ addToken(Token.LITERAL_STRING_DOUBLE_QUOTE); }
	{UnclosedStringLiteral}			{ addToken(Token.ERROR_STRING_DOUBLE); addNullToken(); return firstToken; }
	{ErrorStringLiteral}			{ addToken(Token.ERROR_STRING_DOUBLE); }

	/* Comment Literals. */
	{MLCBegin}					{ start = zzMarkedPos-2; yybegin(MLC); }
	{LineCommentBegin}			{ start = zzMarkedPos-2; yybegin(EOL_COMMENT); }

	/* Separators. */
	"(" |
	")" |
	"[" |
	"]" |
	"{" |
	"}"							{ addToken(Token.SEPARATOR); }

	/* Operators. */
	{Trigraph} |
	"=" |
	"+" |
	"-" |
	"*" |
	"/" |
	"%" |
	"~" |
	"<" |
	">" |
	"<<" |
	">>" |
	"==" |
	"+=" |
	"-=" |
	"*=" |
	"/=" |
	"%=" |
	">>=" |
	"<<=" |
	"^" |
	"&" |
	"&&" |
	"|" |
	"||" |
	"?" |
	":" |
	"," |
	"!" |
	"++" |
	"--" |
	"." |
	","							{ addToken(Token.OPERATOR); }

	/* Numbers */
	{IntegerLiteral}				{ addToken(Token.LITERAL_NUMBER_DECIMAL_INT); }
	{HexLiteral}					{ addToken(Token.LITERAL_NUMBER_HEXADECIMAL); }
	{FloatLiteral}					{ addToken(Token.LITERAL_NUMBER_FLOAT); }
	{ErrorNumberFormat}				{ addToken(Token.ERROR_NUMBER_FORMAT); }

	/* Some lines will end in '\' to wrap an expression. */
	"\\"							{ addToken(Token.IDENTIFIER); }

	{ErrorIdentifier}				{ addToken(Token.ERROR_IDENTIFIER); }

	/* Other punctuation, we'll highlight it as "identifiers." */
	";"							{ addToken(Token.IDENTIFIER); }

	/* Ended with a line not in a string or comment. */
	<<EOF>>						{ addNullToken(); return firstToken; }

	/* Catch any other (unhandled) characters and flag them as bad. */
	.							{ addToken(Token.ERROR_IDENTIFIER); }

}

<MLC> {

	[^hwf\n\*]+					{}
	{URL}						{ int temp=zzStartRead; addToken(start,zzStartRead-1, Token.COMMENT_MULTILINE); addHyperlinkToken(temp,zzMarkedPos-1, Token.COMMENT_MULTILINE); start = zzMarkedPos; }
	[hwf]						{}

	\n							{ addToken(start,zzStartRead-1, Token.COMMENT_MULTILINE); return firstToken; }
	{MLCEnd}						{ yybegin(YYINITIAL); addToken(start,zzStartRead+1, Token.COMMENT_MULTILINE); }
	\*							{}
	<<EOF>>						{ addToken(start,zzStartRead-1, Token.COMMENT_MULTILINE); return firstToken; }

}


<EOL_COMMENT> {
	[^hwf\n]+				{}
	{URL}					{ int temp=zzStartRead; addToken(start,zzStartRead-1, Token.COMMENT_EOL); addHyperlinkToken(temp,zzMarkedPos-1, Token.COMMENT_EOL); start = zzMarkedPos; }
	[hwf]					{}
	\n						{ addToken(start,zzStartRead-1, Token.COMMENT_EOL); addNullToken(); return firstToken; }
	<<EOF>>					{ addToken(start,zzStartRead-1, Token.COMMENT_EOL); addNullToken(); return firstToken; }
}
