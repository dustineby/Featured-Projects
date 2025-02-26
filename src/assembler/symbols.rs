use std::collections::HashMap;

pub struct SymbolTable {
    symbols: HashMap<String, u16>,
    symbol_pointer: u16
}

impl SymbolTable {

    // spawn with pointer at 0 and immediately add predefined labels
    pub fn new() -> Self {
        let mut sym_table = SymbolTable {
            symbols: HashMap::new(),
            symbol_pointer: 0,
        };
        sym_table.populate_predef();
        sym_table
    }

    // half pass to find labels for loop pointers, add to table, and remove
    // count lines to determine where to point label calls
    // also delete those labels by not adding them to output vector
    pub fn build_labels(&mut self, lines: &[String]) -> Vec<String> {
        let mut no_labels = Vec::new();
        
        for line in lines {
            if line.starts_with('(') {
                self.symbols.insert(
                    line.trim_matches(|c| c == '(' || c == ')').to_string(),
                    self.symbol_pointer
                );
            } else {
                no_labels.push(line.clone());
                self.symbol_pointer += 1;
            }
        }

        no_labels
    }

    // half pass to find variables and label pointers, 
    // and change them to the necessary address
    pub fn resolve_vars(&mut self, lines: Vec<String>) -> Vec<String> {
        self.symbol_pointer = 16;
        let mut post_vars = Vec::new();
        
        for line in lines {
            if let Some(symbol) = line.strip_prefix('@') {
                
                if symbol.chars().all(|c| c.is_ascii_digit()) {
                    post_vars.push(line);
                    continue;
                }
    
                let address;
                if self.symbols.contains_key(symbol) {
                    address = *self.symbols.get(symbol).unwrap();
                } else {
                    address = self.symbol_pointer;
                    self.symbols.insert(symbol.to_string(), self.symbol_pointer);
                    self.symbol_pointer += 1;
                }
    
                post_vars.push(format!("@{}", address));
            } else {    
                post_vars.push(line);
            }
        }

        post_vars
    }
 
    pub fn populate_predef(&mut self) {
        let predefined = [
            ("SP", 0),
            ("LCL", 1),
            ("ARG", 2),
            ("THIS", 3),
            ("THAT", 4),
            ("SCREEN", 16384),
            ("KBD", 24576),
        ];

        for val in 0..16 {
            self.symbols.insert(format!("R{}", val), val);
        }

        for &(symbol, addr) in &predefined {
            self.symbols.insert(symbol.to_string(), addr);
        }
    }
}

