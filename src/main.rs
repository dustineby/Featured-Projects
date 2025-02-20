mod parser;

use std::env;
use std::fs;
use parser::*;
use std::io::{BufWriter, Write};
use std::path::Path;

// main fn
fn main() {
    let args: Vec<String> = env::args().collect();
    if args.len() != 2 {
        eprintln!("Incorrect args");
        return;
    }

    let input_file = &args[1];
    let output_file = input_file.replace(".asm", ".bin");

    let asm_code = fs::read_to_string(input_file).expect("Failed to read input file");

    let asm_lines: Vec<String> = asm_code.lines()
        .filter(|line| !line.is_empty() && !line.starts_with("//"))
        .map(String::from)
        .collect();

    for asm in asm_lines {

    }

    
    // let test_line = parser::parse_asm(&asm_lines[0]);

    // println!("{:?}", test_line);

    // let test_bin = parser::write_bin(test_line.expect("reason"));

    // println!("{:#016b}", test_bin);
}

// Writer, could use std::io's BufWriter to buffer writes and call flush to write them all at once
// But we expect all of our test cases to be simple enough that single write calls are ok
fn write_bin(output_path: &Path, instructions: &[u16]) -> std::io::Result<()> {
    let file = File::create(output_path)?;
    let mut writer = BufWriter::new(file);
    
    for &instruction in instructions {
        writer.write_all(&instruction.to_be_bytes())?;
    }
    
    writer.flush()?;
    Ok(())
}

fn write_bin(output_path: &Path, instructions: &[u16]) -> std::io::Result<()> {
    fs::write("filename.txt", "Hello, world!")?;
    Ok(())
}