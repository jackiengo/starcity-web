(ns public.states
  (:require [clojure.spec.alpha :as s]))

;; =============================================================================
;; Data
;; =============================================================================

(def +states+
  {"WI" "Wisconsin" "SC" "South Carolina" "MN" "Minnesota" "NV" "Nevada" "NM" "New Mexico" "NE" "Nebraska" "AK" "Alaska" "NH" "New Hampshire" "ME" "Maine" "NY" "New York" "TN" "Tennessee" "FL" "Florida" "IA" "Iowa" "GA" "Georgia" "IL" "Illinois" "RI" "Rhode Island" "VA" "Virginia" "MI" "Michigan" "PA" "Pennsylvania" "UT" "Utah" "WY" "Wyoming" "SD" "South Dakota" "MO" "Missouri" "KY" "Kentucky" "CT" "Connecticut" "AR" "Arkansas" "ID" "Idaho" "MA" "Massachusetts" "OK" "Oklahoma" "AL" "Alabama" "VT" "Vermont" "MS" "Mississippi" "CA" "California" "LA" "Louisiana" "DE" "Delaware" "WA" "Washington" "KS" "Kansas" "MD" "Maryland" "ND" "North Dakota" "TX" "Texas" "OR" "Oregon" "NC" "North Carolina" "AZ" "Arizona" "IN" "Indiana" "WV" "West Virginia" "CO" "Colorado" "HI" "Hawaii" "MT" "Montana" "NJ" "New Jersey" "OH" "Ohio"})

;; =============================================================================
;; API
;; =============================================================================

(def abbreviations
  "List of state abbreviations."
  (sort (keys +states+)))

(def states
  "List of states."
  (sort (vals +states+)))

(defn state
  "Look up a state from its abbreviation."
  [abbr]
  (get +states+ abbr))

(s/def ::abbreviation (set abbreviations))
(s/def ::state (set states))

(s/fdef state
        :args (s/cat :abbreviation ::abbreviation)
        :ret ::state)
