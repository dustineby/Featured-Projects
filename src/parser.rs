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


pub fn parse_asm(asm_line: &str) -> Result<Instruction, String> {
    let asm_line = asm_line.trim();

    match asm_line.chars().next() {
        Some('@') => Ok(instructions::parse_addr(asm_line)),
        Some(_)   => Ok(instructions::parse_cmd(asm_line)),
        None => Err("Empty line".to_string())
    }
}


pub fn write_bin(inst: Instruction) -> u16 {
    match inst {
        Instruction::AInstruction(addr) => instructions::write_addr(addr),
        Instruction::CInstruction{dest,comp,jump} => 
            instructions::write_cmd(dest.as_deref(),&comp,jump.as_deref())
    }
}