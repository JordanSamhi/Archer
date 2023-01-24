import pandas as pd


goodware_file = "goodware_results.lst"
malware_file = "malware_results.lst"


## MALWARE
print("#=============#")
print("|   MALWARE   |")
print("#=============#")
df_malware = pd.read_csv(malware_file, delimiter=";", names=['sha', 'analysis_time',
                                                     'instrumentation_time', 'taint_analysis_time',
                                                     'new_edges', 'new_nodes',
                                                     'nodes_in_cg', 'edges_in_cg',
                                                     'extra_stmt', 'extra_stmt_percent',
                                                     'timeout', 'constraints'])

print("On 350 malware")
print("Before Archer")
print(f"Number of Nodes: {round(df_malware['nodes_in_cg'].mean() - df_malware['new_nodes'].mean())}")
print(f"Number of Edges: {round(df_malware['edges_in_cg'].mean() - df_malware['new_edges'].mean())}")
print("After Archer")
print(f"Number of Nodes: {round(df_malware['nodes_in_cg'].mean())}")
print(f"Number of Edges: {round(df_malware['edges_in_cg'].mean())}")
print("Difference")
print(f"Number of Nodes: {round(df_malware['new_nodes'].mean())} ({round(df_malware['new_nodes'].mean() * 100 / (df_malware['nodes_in_cg'].mean() - df_malware['new_nodes'].mean()))}%)")
print(f"Number of Edges: {round(df_malware['new_edges'].mean())} ({round(df_malware['new_edges'].mean() * 100 / (df_malware['edges_in_cg'].mean() - df_malware['new_edges'].mean()))}%)")



## GOODWARE
print("#==============#")
print("|   GOODWARE   |")
print("#==============#")
df_goodware = pd.read_csv(goodware_file, delimiter=";", names=['sha', 'analysis_time',
                                                      'instrumentation_time', 'taint_analysis_time',
                                                      'new_edges', 'new_nodes',
                                                      'nodes_in_cg', 'edges_in_cg',
                                                      'extra_stmt', 'extra_stmt_percent',
                                                      'timeout', 'constraints'])
print("On 576 goodware")
print("Before Archer")
print(f"Number of Nodes: {round(df_goodware['nodes_in_cg'].mean() - df_goodware['new_nodes'].mean())}")
print(f"Number of Edges: {round(df_goodware['edges_in_cg'].mean() - df_goodware['new_edges'].mean())}")
print("After Archer")
print(f"Number of Nodes: {round(df_goodware['nodes_in_cg'].mean())}")
print(f"Number of Edges: {round(df_goodware['edges_in_cg'].mean())}")
print("Difference")
print(f"Number of Nodes: {round(df_goodware['new_nodes'].mean())} ({round(df_goodware['new_nodes'].mean() * 100 / (df_goodware['nodes_in_cg'].mean() - df_goodware['new_nodes'].mean()))}%)")
print(f"Number of Edges: {round(df_goodware['new_edges'].mean())} ({round(df_goodware['new_edges'].mean() * 100 / (df_goodware['edges_in_cg'].mean() - df_goodware['new_edges'].mean()))}%)")


print()
print()

print(f"Extra Statements Goodware mean: {round(df_goodware['extra_stmt'].mean())}")
print(f"Extra Statements Goodware median: {round(df_goodware['extra_stmt'].median())}")
print(f"Extra Statements Malware mean: {round(df_malware['extra_stmt'].mean())}")
print(f"Extra Statements Malware median: {round(df_malware['extra_stmt'].median())}")


print()
print()

print(f"Overhead Goodware average: {round(df_goodware['instrumentation_time'].mean())} (+{round(df_goodware['instrumentation_time'].mean() * 100 / df_goodware['analysis_time'].mean())}%)")
print(f"Overhead Malware average: {round(df_malware['instrumentation_time'].mean())} (+{round(df_malware['instrumentation_time'].mean() * 100 / df_malware['analysis_time'].mean())}%)")

df_malware["overall"] = df_malware["instrumentation_time"] * 100 / df_malware['analysis_time']
df_goodware["overall"] = df_goodware["instrumentation_time"] * 100 / df_goodware['analysis_time']
print(f"Overhead Goodware median: {round(df_goodware['overall'].median())}")
print(f"Overhead Malware median: {round(df_malware['overall'].median())}")
