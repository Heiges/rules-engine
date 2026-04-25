import { createContext, useContext, useState } from 'react'
import type { ReactNode } from 'react'

interface RulesetContextType {
  currentRuleset: string | null
  setCurrentRuleset: (name: string | null) => void
}

const RulesetContext = createContext<RulesetContextType | null>(null)

export function RulesetProvider({ children }: { children: ReactNode }) {
  const [currentRuleset, setCurrentRuleset] = useState<string | null>(null)

  return (
    <RulesetContext.Provider value={{ currentRuleset, setCurrentRuleset }}>
      {children}
    </RulesetContext.Provider>
  )
}

export function useRuleset(): RulesetContextType {
  const ctx = useContext(RulesetContext)
  if (!ctx) throw new Error('useRuleset must be used within RulesetProvider')
  return ctx
}
