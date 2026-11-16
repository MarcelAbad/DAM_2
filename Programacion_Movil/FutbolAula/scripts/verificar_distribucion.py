import unittest, zipfile, re, json
import xml.etree.ElementTree as ET
from pathlib import Path

class DistributionChecks(unittest.TestCase):
    archives = ("ALUMNO.ZIP", "PROFESOR.ZIP")
    def contents(self, archive):
        with zipfile.ZipFile(archive) as z:
            return {"/".join(n.split("/")[1:]): z.read(n).decode("utf-8") for n in z.namelist()}
    def test_01_zip_integrity(self):
        for a in self.archives:
            with zipfile.ZipFile(a) as z: self.assertIsNone(z.testzip())
    def test_02_examples_count(self):
        for a in self.archives: self.assertEqual(16,sum("/ejemplos/" in n and n.endswith(".kt") for n in self.contents(a)))
    def test_03_exercises_count(self):
        for a in self.archives: self.assertEqual(16,sum("/evaluables/" in n and n.endswith(".kt") for n in self.contents(a)))
    def test_04_examples_identical(self):
        a,b=map(self.contents,self.archives)
        self.assertEqual({k:v for k,v in a.items() if "/ejemplos/" in k},{k:v for k,v in b.items() if "/ejemplos/" in k})
    def test_05_only_student_has_todos(self):
        a,b=map(self.contents,self.archives)
        self.assertEqual(16,sum(v.count('TODO("') for k,v in a.items() if "/evaluables/" in k))
        self.assertEqual(0,sum(v.count('TODO("') for k,v in b.items() if "/evaluables/" in k))
    def test_06_no_teacher_guide_in_student(self):
        self.assertFalse(any("SOLUCIONARIO" in k for k in self.contents(self.archives[0])))
    def test_07_documents(self):
        for a in self.archives:
            d=self.contents(a)
            for i in range(1,17):
                self.assertIn(f"docs/E{i:02d}.md",d)
                self.assertEqual(d[f"docs/E{i:02d}.md"],d[f"app/src/main/assets/E{i:02d}.md"])
    def test_08_test_count(self):
        for a in self.archives:
            d=self.contents(a)
            self.assertEqual(54,sum(v.count("@Test") for k,v in d.items() if "/src/test/" in k))
    def test_09_instrumentation_count(self):
        for a in self.archives:
            self.assertEqual(3,sum(v.count("@Test") for k,v in self.contents(a).items() if "/src/androidTest/" in k))
    def test_10_manifest(self):
        ns="{http://schemas.android.com/apk/res/android}"
        for a in self.archives:
            root=ET.fromstring(self.contents(a)["app/src/main/AndroidManifest.xml"])
            self.assertEqual(".MainActivity",root.find("application/activity").get(ns+"name"))
            self.assertEqual("false",root.find("application/provider").get(ns+"exported"))
    def test_11_data_json(self):
        for a in self.archives:
            for k,v in self.contents(a).items():
                if k.endswith(".json"): self.assertIsNotNone(json.loads(v))
    def test_12_main_calls_all_exercises(self):
        for a in self.archives:
            d=self.contents(a); main=d["app/src/main/java/es/aula/futbol/MainActivity.kt"]
            for k,v in d.items():
                if "/evaluables/" in k:
                    obj=re.search(r"object (E\d+\w+)",v).group(1)
                    self.assertIn(obj+".",main)
    def test_13_registry(self):
        for a in self.archives:
            d=self.contents(a); reg=d["app/src/main/java/es/aula/futbol/Academy.kt"]
            for k,v in d.items():
                if "/evaluables/" in k or "/ejemplos/" in k:
                    obj=re.search(r"object ([EX]\d+\w+)",v).group(1)
                    self.assertIn(obj+".demo()",reg)
    def test_14_no_duplicate_source_objects(self):
        for a in self.archives:
            objects=[]
            for k,v in self.contents(a).items():
                if "/src/main/" in k and k.endswith(".kt"):
                    objects+=re.findall(r"^(?:data )?(?:class|object) (\w+)",v,re.M)
            self.assertEqual(len(objects),len(set(objects)))
    def test_15_core_identical(self):
        a,b=map(self.contents,self.archives)
        for k,v in a.items():
            if "/src/main/" in k and "/evaluables/" not in k: self.assertEqual(v,b[k])
    def test_16_no_path_traversal(self):
        for a in self.archives:
            with zipfile.ZipFile(a) as z:
                self.assertTrue(all(not n.startswith("/") and ".." not in Path(n).parts for n in z.namelist()))

if __name__ == "__main__": unittest.main(verbosity=2)
