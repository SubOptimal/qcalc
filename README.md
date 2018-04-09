# qcalc
A Java TUI calculator.

Qcalc is a TUI calculator with a friendly user interface and custom definition support. It takes flags and a function as arguments,
and outputs the result. Qcalc is licensed under the MIT license. See [LICENSE](/LICENSE) for more details. For pre-built releases, look [here](https://github.com/paroxayte/qcalc/releases). For detailed documentation check the [docs page](https://paroxayte.github.io/qcalc/).
<br />
<br />
**syntax:** [optional flags] "function". Eg `qcalc -v "sqrt(5^2+8^2)"`

**supported flags:**
- -v Verbose. Prints out each state change of the function as it is being solved.
- -D Define. Defines a function which can be used in functions. 
  **Syntax:** -D "funcName(arbitary, number, ofparams) -> arbitary*number*ofparams"
- -l List. Lists all user definitions.
- -rm Remove. Removes the definition contained on a given line. Use -l to get the line number. **Syntax:** -rm lineNumber. 
  eg `qcalc -rm 2`
  The plugin script is saved in the directory the jar is placed in.
  <br />
  **Build command:** `mvn install` 
  <br />
  **Installation(linux):** 
  - `chmod +x qcalc`
  - move somewhere on your PATH or use softlink. Eg `mv ./qcalc /usr/local/lib/qcalc/` `ln -s /usr/local/lib/qcalc/qcalc /usr/local/bin`
 # screenshots
 ![simple usage](/simple.png?raw=true "simple")
 ![flag usage](/flagExpose.png?raw=true "flag usages")
