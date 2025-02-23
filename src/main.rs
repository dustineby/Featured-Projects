mod assembler;

use std::env;
use anyhow::Result;
use anyhow::anyhow;


// main fn
fn main()  -> Result<()> {
    let args: Vec<String> = env::args().collect();
    if args.len() != 2 {
       return Err(anyhow!("Invalid argument count"));
    }

    // grab passed .asm file, create new .bin file and writer
    let input = &args[1];
    let output = input.replace(".asm", ".bin");

    assembler::assemble(&input, &output)?;
    Ok(())
}
