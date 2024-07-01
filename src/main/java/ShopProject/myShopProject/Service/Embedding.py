
from sentence_transformers import SentenceTransformer
import sys
model = SentenceTransformer("sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2")

sentence = sys.argv[1:]
result = model.encode(sentence)
result.tolist()
print(result[0][:10])


