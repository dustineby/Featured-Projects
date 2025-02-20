mod instructions;

#[derive(Debug)]
pub enum Instruction {
    AInstruction (u16),
    CInstruction {
        dest: Option<String>,
        comp: String,
        jump: Option<String>
    }
}


pub fn parse_asm(asm_line: &str) -> Option<Instruction> {
    let asm_line = asm_line.trim();

    match asm_line.chars().next() {
        Some('@') => Some(instructions::parse_addr(asm_line)),
        Some(_)   => Some(instructions::parse_cmd(asm_line)),
        None => None
    }
}