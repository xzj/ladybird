(ns ladybird.test.db.patch.korma)


;; TODO: rewrite this test
(comment

  
(use 'ladybird.db.load :reload)
(load-db-file "resources/db.def")
(require '[ladybird.db.core :as dc])
(def db-def (:conn-def (dc/get-cur-conn)))
(use 'ladybird.db.patch.korma :reload)
(use 'ladybird.data.cond)
(def tt {:condition (make (< :id 10)) :as-table {:as-table "tmp" :condition (make (= :valid "T"))}})
(select tt () {:db db-def})
(def r *1)
(map :id r)
(select tt () {:db db-def :joins {:p [:inner "p" [[:p.id :pid]] (make (= :p.id :id))]} :join-with [:p]})
(def p {:as-table "p" :condition (make (= :name "abc"))})
(select tt () {:db db-def :joins {:p [:left p [[:p.id :pid]] (make (= :p.id :id))]} :join-with [:p]})
(def r *1)
(count r)
(map :id r)
(map :pid r)

(def p2 {:as-table "p" :condition (make (!= :name "abc"))})
(select tt () {:db db-def :joins {:p [:left p2 [[:p.id :pid]] (make (= :p.id :id))]} :join-with [:p]})
(def r2 *1)
(map :pid r2)

(def tt {:condition (make (< :id 10)) :as-table {:as-table "tmp" :condition (make (= :valid "T"))} :joins {:p [:left p2 [[:p.id :pid]] (make (= :p.id :id))]} :join-with [:p]})
(select tt () {:db db-def})

(def tt2 {:condition (make (< :id 10)) :as-table {:as-table "tmp" :condition (make (= :valid "T"))} :joins {:p [:inner p [[:p.id :pid]] (make (= :p.id :id))]} :join-with [:p]})
(select tt2 () {:db db-def})

(def t {:condition (make (< :id 10)) :as-table {:as-table "tmp" :condition (make (= :valid "T"))}})
(select t () {:db db-def :joins {:tt2 [:inner tt2 [] (make (= :id :tt2.id))]} :join-with [:tt2]})
(select t () {:db db-def :joins {:tt2 [:inner tt2 [:tt2.pid [:tt2.id :tid]] (make (= :id :tt2.id))]} :join-with [:tt2]})


(load-db-file "../sfajx/db/db-dev.def")
(def db-def (:conn-def (dc/get-cur-conn)))
(def p {:condition () :as-table "member_prof" :aggregate [[:count :id] :cnt :unit_id] :fields [:unit_id] :db db-def})
(select p () {:db db-def})
(count *1)
(select "member_unit" () {:join-with [:p] :joins {:p [:left p [:p.unit_id :p.cnt] '(= :p.unit_id :id)]} :db db-def :fields [:id]})
(def r *1)
(count r)

(select "member_prof" () {:aggregate [[:count :id] :cnt] :group-by [:unit_id] :db db-def :fields [:unit_id]})
(count *1)

(def p2 {:condition () :as-table "member_prof" :aggregate [[:count :id] :cnt :unit_id] :fields [[:unit_id :unitId]] :db db-def})
(select "member_unit" () {:join-with [:p] :joins {:p [:left p2 [:p.unitId :p.cnt] '(= :p.unitId :id)]} :db db-def :fields [:id]})
(select "member_unit" () {:join-with [:p] :joins {:p [:left p2 [[:p.unitId :unitid] :p.cnt] '(= :p.unitId :id)]} :db db-def :fields [:id]})

(def u {:condition () :as-table "member_unit" :fields [[:id unitId] :unit_name] :db db-def})
(select u () {:db db-def})
(def uu {:condition () :as-table u :fields [[:unitId :Id] :unit_name] :db db-def})
(select uu () {:db db-def})

)
