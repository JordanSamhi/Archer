import pandas as pd

def process_constraints(file_path):
    goodwares_f = "../datasets/goodwares"
    malwares_f = "../datasets/malwares"
    number_of_constraints = 0
    number_of_constraints_per_app_tab = []
    goodwares = set()
    malwares = set()
    with open(goodwares_f, 'r') as f:
        lines = f.readlines()
        for line in lines:
            l = line.strip()
            goodwares.add(l)
    with open(malwares_f, 'r') as f:
        lines = f.readlines()
        for line in lines:
            l = line.strip()
            malwares.add(l)
    constraints_dic = {}
    shas_with_constraints = set()
    with open(file_path, 'r') as f:
        lines = f.readlines()
        for line in lines:
            number_of_constraints_per_app = 0
            l = line.rstrip()
            split = l.split(";")
            sha = split[0]
            edges = split[11]
            edges_split = edges.split("#")
            for edge in edges_split:
                edge_split = edge.split("|")
                if len(edge_split) > 3:
                    constraints = edge_split[3]
                    if constraints:
                        if sha in goodwares or sha in malwares:
                            shas_with_constraints.add(sha)
                        constraints_split = constraints.split("%")
                        for constraint in constraints_split:
                            if(constraint):
                                number_of_constraints += 1
                                number_of_constraints_per_app += 1
                                constraint_split = constraint.split("&")
                                c = constraint_split[0]
                                v = constraint_split[1]
                                if not c in constraints_dic:
                                    constraints_dic[c] = 0
                                constraints_dic[c] = constraints_dic[c] + 1
            if number_of_constraints_per_app != 0:
                number_of_constraints_per_app_tab.append(number_of_constraints_per_app)
    for k,v in constraints_dic.items():
        print(f"{k} : {v}")
    print(f"Count of apps with constraints: {len(shas_with_constraints)}")
    print(f"Number of constraints found: {number_of_constraints}")
    df_constraints_per_app = pd.DataFrame(data=number_of_constraints_per_app_tab)
    return df_constraints_per_app
    
    

print("GOODWARES")
process_constraints("goodware_results.lst")
print()
print("=======")
print()
print("MALWARES")
process_constraints("malware_results.lst")
