use super::Instruction;


pub fn encode_bin(inst: Instruction) -> u16 {
    match inst {
        Instruction::AInstruction(addr) => encode_addr(addr),
        Instruction::CInstruction{dest,comp,jump} => 
            encode_cmd(dest.as_deref(),&comp,jump.as_deref())
    }
}


pub fn encode_addr(addr: u16) -> u16 {
    0b0000_0000_0000_0000 | addr
}


pub fn encode_cmd(dest: Option<&str>, comp: &str, jump: Option<&str>) -> u16 {
    let dest_bits = encode_dest(dest);
    let comp_bits = encode_comp(comp);
    let jump_bits = encode_jump(jump);
    0b1110_0000_0000_0000 | comp_bits | dest_bits | jump_bits
}


pub fn encode_dest(dest: Option<&str>) -> u16 {
    match dest {
        None => 0b0000_0000_0000_0000,
        Some("M") => 0b0000_0000_0000_1000,
        Some("D") => 0b0000_0000_0001_0000,
        Some("A") => 0b0000_0000_0010_0000,
        Some("MD") => 0b0000_0000_0001_1000,
        Some("AM") => 0b0000_0000_0010_1000,
        Some("AD") => 0b0000_0000_0011_0000,
        Some("AMD") => 0b0000_0000_0011_1000,
        Some(_) => 0b0000_0000_0000_0000
    }
}


pub fn encode_comp(comp: &str) -> u16 {
    match comp {
        "0" => 0b0000_1010_1000_0000,
        "1" => 0b0000_1111_1100_0000,
        "-1" => 0b0000_1110_1000_0000,
        
        "D" => 0b0000_0011_0000_0000,
        "A" => 0b0000_1100_0000_0000,
        "M" => 0b0001_1100_0000_0000,

        "!D" => 0b0000_0011_0100_0000,
        "!A" => 0b0000_1100_0100_0000,
        "!M" => 0b0001_1100_0100_0000,

        "-D" => 0b0000_0011_1100_0000,
        "-A" => 0b0000_1100_1100_0000,
        "-M" => 0b0001_1100_1100_0000,
        
        "D+1" => 0b0000_0111_1100_0000,
        "A+1" => 0b0000_1101_1100_0000,
        "M+1" => 0b0001_1101_1100_0000,

        "D-1" => 0b0000_0011_1000_0000,
        "A-1" => 0b0000_1100_1000_0000,
        "M-1" => 0b0001_1100_1000_0000,

        "D+A" => 0b0000_0000_1000_0000,
        "D+M" => 0b0001_0000_1000_0000,

        "D-A" => 0b0000_0100_1100_0000,
        "D-M" => 0b0001_0100_1100_0000,
        "A-D" => 0b0000_0001_1100_0000,
        "M-D" => 0b0001_0001_1100_0000,

        "D&A" => 0b0000_0000_0000_0000,
        "D&M" => 0b0001_0000_0000_0000,

        "D|A" => 0b0000_0101_0100_0000,
        "D|M" => 0b0001_0101_0100_0000,

        _ => 0b0000_0000_0000_0000
    }
}


pub fn encode_jump(jump: Option<&str>) -> u16 {
    match jump {
        None => 0b0000_0000_0000_0000,
        Some("JGT") => 0b0000_0000_0000_0001,
        Some("JEQ") => 0b0000_0000_0000_0010,
        Some("JGE") => 0b0000_0000_0000_0011,
        Some("JLT") => 0b0000_0000_0000_0100,
        Some("JNE") => 0b0000_0000_0000_0101,
        Some("JLE") => 0b0000_0000_0000_0110,
        Some("JMP") => 0b0000_0000_0000_0111,
        Some(_) => 0b0000_0000_0000_0000
    }
}
