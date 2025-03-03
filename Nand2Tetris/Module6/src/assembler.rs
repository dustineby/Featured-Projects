mod parser;
mod encoder;
mod symbols;

use std::fs;
use std::io::Write;
use std::io;

// instruction enum, used by poth parser and encoder
#[derive(Debug)]
pub enum Instruction {
    AInstruction (u16),
    CInstruction {
        dest: Option<String>,
        comp: String,
        jump: Option<String>
    }
}

// assembler, spawns new file, reads in text from .asm file,
// cleans it, and stores it in a vector. Then runs it through 
// first half pass (label collection), second half pass (resolving variables),
// and second full pass, converting .asm to Instructions, 
// and enconding Instructions into binary.
pub fn assemble(input: &str, output: &str) -> io::Result<()> {

    let mut output_file = fs::File::create(output)?;

    let asm_code = fs::read_to_string(input).expect("Failed to read input file");
    let asm_lines: Vec<String> = asm_code.lines()
        .map(str::trim)
        .filter(|line| !line.is_empty() && !line.starts_with('/'))
        .map(String::from)
        .collect();

    let mut table = symbols::SymbolTable::new();
    let labels = table.build_labels(&asm_lines);
    let vars = table.resolve_vars(labels);

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