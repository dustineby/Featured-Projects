mod parser;

use std::env;
use std::fs;
use parser::*;
use std::io::{BufWriter, Write};
use std::path::Path;
use anyhow::{Result, anyhow};

// main fn
fn main()  -> Result<()> {
    let args: Vec<String> = env::args().collect();
    if args.len() != 2 {
       return Err(anyhow!("Invalid argument count"));
    }

    // grab passed .asm file, create new .bin file and writer
    let input_file = &args[1];
    let output_name = input_file.replace(".asm", ".bin");
    let mut output_file = fs::File::create(output_name)?;

    // read .asm file and turn into vector of lines, with empty lines and comments removed
    let asm_code = fs::read_to_string(input_file).expect("Failed to read input file");
    let asm_lines: Vec<String> = asm_code.lines()
        .filter(|line| !line.is_empty() && !line.starts_with("//"))
        .map(String::from)
        .collect();

    // iterate over vector of lines, paring and then writing each
    for line in &asm_lines {
        match parser::parse_asm(line) {
            Ok(next_instruction) => {
                let bin_line = parser::write_bin(next_instruction);
                println!("{:016b}", bin_line);
                writeln!(output_file, "{:016b}", bin_line);
            }
            Err(e) => {
                println!("error: {}", e);
            }
        }
    }

    Ok(())

}
