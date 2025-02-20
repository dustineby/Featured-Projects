use super::Instruction;

pub fn parse_addr(line: &str) -> Instruction {
    let value = line[1..].parse::<u16>();
    Instruction::AInstruction(value.expect("Err23"))
}

pub fn parse_cmd(line: &str) -> Instruction {

    let (dest, rest) = match line.split_once('=') {
        Some((dest, rest)) => (Some(dest.to_string()), rest),
        None => (None, line),
    };

    let (comp, jump) = match rest.split_once(';') {
        Some((comp, jump)) => (comp.to_string(), Some(jump.to_string())),
        None => (rest.to_string(), None),
    };

    Instruction::CInstruction { dest, comp, jump }
}
