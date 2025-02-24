mod parser;
mod encoder;
mod symbols;

use std::fs;
use std::io::Write;
use std::io;

#[derive(Debug)]
pub enum Instruction {
    AInstruction (u16),
    CInstruction {
        dest: Option<String>,
        comp: String,
        jump: Option<String>
    }
}


pub fn assemble(input: &str, output: &str) -> io::Result<()> {

    let mut output_file = fs::File::create(output)?;

    // read .asm file and turn into vector of lines, with empty lines and comments removed
    let asm_code = fs::read_to_string(input).expect("Failed to read input file");
    let asm_lines: Vec<String> = asm_code.lines()
        .map(str::trim)
        .filter(|line| !line.is_empty() && !line.starts_with('/'))
        .map(String::from)
        .collect();

    let mut table = symbols::SymbolTable::new();
    let labels = table.build_labels(&asm_lines);
    let vars = table.resolve_vars(labels);

    // iterate over vector of lines, parsing, encoding, and then writing each
    for line in &vars {
        match parser::parse_asm(line) {
            Ok(next_instruction) => {
                let bin_line = encoder::encode_bin(next_instruction);
                writeln!(output_file, "{:016b}", bin_line)?;
            }
            Err(e) => {
                println!("error: {}", e);
            }
        }
    }

    Ok(())
}